package com.sanpc.roadsense.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.utils.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs


class PotholeDetector(
    context: Context,
    private val zThreshold: Float = 15f,
    private val bufferSize: Int = 5
) : SensorEventListener {

    private val _potholeData = MutableSharedFlow<Pothole>()
    val potholeData = _potholeData.asSharedFlow()

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val zBuffer = ArrayDeque<Float>(bufferSize)

    val username = UserPreferences(context).username

    fun startDetection() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopDetection() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val zValue = event.values[2]
            addToBuffer(zValue)

            val averageZ = zBuffer.average().toFloat()

            if (abs(averageZ) > zThreshold) {
                val timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date(System.currentTimeMillis())
                )

                val potholeEvent = Pothole(
                    username = username,
                    latitude = 1.0, // sample
                    longitude = 1.0,
                    detectionDate = timestamp,
                    syncStatus = false
                )

                _potholeData.tryEmit(potholeEvent)
            }
        }
    }

    private fun addToBuffer(zValue: Float) {
        if (zBuffer.size >= bufferSize) {
            zBuffer.removeFirst()
        }
        zBuffer.addLast(zValue)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
