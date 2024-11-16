package com.sanpc.roadsense.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.utils.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class PotholeDetector(
    context: Context,
    private val locationViewModel: LocationViewModel,
    private val zThreshold: Float = 35f
) : SensorEventListener {

    private val _potholeData = MutableSharedFlow<Pothole>(replay = 1)
    val potholeData = _potholeData.asSharedFlow()

    private val _zValue = MutableSharedFlow<Float>(replay = 1)
    val zValue = _zValue.asSharedFlow()

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val username = UserPreferences(context).username

    fun startDetection() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopDetection() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val zValue = event.values[2]
            _zValue.tryEmit(zValue)

            if (abs(zValue) > zThreshold) {
                val timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())

                locationViewModel.getCurrentLocation()

                val potholeEvent = locationViewModel.currentLocation?.let {
                    Pothole(
                        username = username,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        detectionDate = timestamp,
                        syncStatus = false
                    )
                }

                potholeEvent?.let { _potholeData.tryEmit(it) }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
