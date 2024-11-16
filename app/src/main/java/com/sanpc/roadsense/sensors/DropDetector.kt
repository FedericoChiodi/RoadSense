package com.sanpc.roadsense.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.utils.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class DropDetector(
    context: Context,
    private val locationViewModel: LocationViewModel,
    private val startThreshold: Float = 60f,
    private val endThreshold: Float = 5f
) : SensorEventListener {

    private val _dropData = MutableSharedFlow<Drop>(replay = 1)
    val dropData = _dropData.asSharedFlow()

    private val _gyroLevel = MutableSharedFlow<Float>(replay = 1)
    val gyroLevel = _gyroLevel.asSharedFlow()

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val username = UserPreferences(context).username

    private var isDropStarted = false
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null

    fun startDetection() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopDetection() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val accX = event.values[0]
            val accY = event.values[1]
            val accZ = event.values[2]

            val angle = atan2(accY.toDouble(), sqrt((accX * accX + accZ * accZ).toDouble())) * (180 / Math.PI)

            if(_gyroLevel.tryEmit(angle.toFloat()))
                Log.d("DropDetector", "Inclinazione angolare: $angle")

            if (abs(angle) > startThreshold && !isDropStarted) {
                isDropStarted = true
                locationViewModel.getCurrentLocation()

                locationViewModel.currentLocation?.let { location ->
                    startLatitude = location.latitude
                    startLongitude = location.longitude
                } ?: run {
                    isDropStarted = false
                }
            } else if (abs(angle) < endThreshold && isDropStarted) {
                isDropStarted = false
                locationViewModel.getCurrentLocation()

                locationViewModel.currentLocation?.let { location ->
                    val endLatitude = location.latitude
                    val endLongitude = location.longitude
                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .format(System.currentTimeMillis())

                    if (startLatitude != null && startLongitude != null) {
                        val dropEvent = Drop(
                            username = username,
                            startLatitude = startLatitude!!,
                            startLongitude = startLongitude!!,
                            endLatitude = endLatitude,
                            endLongitude = endLongitude,
                            detectionDate = timestamp,
                            syncStatus = false
                        )
                        _dropData.tryEmit(dropEvent)
                    }
                } ?: run {
                    isDropStarted = false
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}

