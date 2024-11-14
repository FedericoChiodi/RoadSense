package com.sanpc.roadsense.utils

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MQTTClient(
    brokerUrl: String = "b53f4285e0d54a81a0a7263e6b9ab474.s1.eu.hivemq.cloud",
    private val mqttUsername: String = "RoadSenseClient_App",
    private val mqttPassword: String = "VeryStr0ngP455!",
    private val username : String,
    private val dropViewModel: DropViewModel,
    private val potholeViewModel: PotholeViewModel
) {
    private val mqttClient = MqttClient.builder()
        .useMqttVersion5()
        .serverHost(brokerUrl)
        .identifier(mqttUsername)
        .buildAsync()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun connectAndSyncData() {
        mqttClient.toAsync()
            .connectWith()
            .simpleAuth()
            .username(mqttUsername)
            .password(StandardCharsets.UTF_8.encode(mqttPassword))
            .applySimpleAuth()
            .send()
            .whenComplete { _, exception ->
                if (exception == null) {
                    coroutineScope.launch {
                        sendUnsyncedData()
                    }
                } else {
                    exception.printStackTrace()
                }
            }
    }

    private suspend fun sendUnsyncedData() {
        val unsyncedDrops = dropViewModel.getUnsyncedDrops()
        val unsyncedPotholes = potholeViewModel.getUnsyncedPotholes()

        unsyncedDrops.forEach { drop ->
            val message = JSONObject().apply {
                put("start_latitude", drop.startLatitude)
                put("start_longitude", drop.startLongitude)
                put("end_latitude", drop.endLatitude)
                put("end_longitude", drop.endLongitude)
                put("detection_date", drop.detectionDate)
            }.toString()
            publishMessage("roadsense/${username}/drop", message)
            dropViewModel.markAsSynced(drop.id)
        }

        unsyncedPotholes.forEach { pothole ->
            val message = JSONObject().apply {
                put("latitude", pothole.latitude)
                put("longitude", pothole.longitude)
                put("detection_date", pothole.detectionDate)
            }.toString()
            publishMessage("roadsense/${username}/pothole", message)
            potholeViewModel.markAsSynced(pothole.id)
        }
    }

    private fun publishMessage(topic: String, message: String) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray(StandardCharsets.UTF_8))
            .qos(MqttQos.EXACTLY_ONCE)
            .send()
            .whenComplete { _, exception ->
                exception?.printStackTrace()
            }
    }
}
