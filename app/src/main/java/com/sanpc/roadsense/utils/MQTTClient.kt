package com.sanpc.roadsense.utils

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.http.conn.ConnectTimeoutException
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
        .identifier(mqttUsername)
        .serverHost(brokerUrl)
        .serverPort(8883)
        .sslWithDefaultConfig()
        .useMqttVersion5()
        .buildAsync()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun connectAndSyncData() {
        println("Tentativo di connessione al broker MQTT...")
        mqttClient.toAsync()
            .connectWith()
            .simpleAuth()
            .username(mqttUsername)
            .password(StandardCharsets.UTF_8.encode(mqttPassword))
            .applySimpleAuth()
            .send()
            .whenComplete { _, exception ->
                if (exception == null) {
                    println("Connessione riuscita al broker MQTT.")
                    coroutineScope.launch {
                        println("Inizio sincronizzazione dei dati non sincronizzati.")
                        sendUnsyncedData()
                        println("Dati non sincronizzati inviati.")
                    }
                } else {
                    println("Errore nella connessione al broker MQTT: ${exception.message}")
                    println("Cause: ${exception.cause}")
                    exception.printStackTrace()
                    when (exception) {
                        is com.hivemq.client.mqtt.exceptions.ConnectionClosedException -> {
                            println("Il server ha chiuso la connessione senza DISCONNECT.")
                        }
                        is ConnectTimeoutException -> {
                            println("Timeout di connessione al broker.")
                        }
                        else -> {
                            println("Errore generico nella connessione.")
                        }
                    }
                }
            }
    }


    private suspend fun sendUnsyncedData() {
        println("Recupero dei dati non sincronizzati...")
        val unsyncedDrops = dropViewModel.getUnsyncedDrops()
        val unsyncedPotholes = potholeViewModel.getUnsyncedPotholes()

        if (unsyncedDrops.isEmpty() && unsyncedPotholes.isEmpty()) {
            println("Nessun dato non sincronizzato trovato.")
        }

        unsyncedDrops.forEach { drop ->
            println("Invio del dato drop: ${drop.id}...")
            val message = JSONObject().apply {
                put("start_latitude", drop.startLatitude)
                put("start_longitude", drop.startLongitude)
                put("end_latitude", drop.endLatitude)
                put("end_longitude", drop.endLongitude)
                put("detection_date", drop.detectionDate)
            }.toString()
            publishMessage("roadsense/${username}/drop", message)
            println("Dato drop ${drop.id} inviato, marcato come sincronizzato.")
            dropViewModel.markAsSynced(drop.id)
        }

        unsyncedPotholes.forEach { pothole ->
            println("Invio del dato pothole: ${pothole.id}...")
            val message = JSONObject().apply {
                put("latitude", pothole.latitude)
                put("longitude", pothole.longitude)
                put("detection_date", pothole.detectionDate)
            }.toString()
            publishMessage("roadsense/${username}/pothole", message)
            println("Dato pothole ${pothole.id} inviato, marcato come sincronizzato.")
            potholeViewModel.markAsSynced(pothole.id)
        }
    }

    private fun publishMessage(topic: String, message: String) {
        println("Invio messaggio al topic $topic...")
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray(StandardCharsets.UTF_8))
            .qos(MqttQos.EXACTLY_ONCE)
            .send()
            .whenComplete { _, exception ->
                if (exception == null) {
                    println("Messaggio inviato al topic $topic con successo.")
                } else {
                    println("Errore nell'invio del messaggio al topic $topic: ${exception.message}")
                    exception.printStackTrace()
                }
            }
    }
}
