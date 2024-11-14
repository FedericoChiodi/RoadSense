package com.sanpc.roadsense.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.utils.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class DropDetector(
    context: Context,
    private val locationViewModel: LocationViewModel,
    private val startThreshold: Float = 30f,
    private val endThreshold: Float = 15f
) : SensorEventListener {

    private val _dropData = MutableSharedFlow<Drop>()
    val dropData = _dropData.asSharedFlow()

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    val username = UserPreferences(context).username

    private var isDropStarted = false
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null

    fun startDetection() {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopDetection() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val zRotationRate = event.values[2]

            if (abs(zRotationRate) > startThreshold && !isDropStarted) {
                isDropStarted = true
                locationViewModel.getCurrentLocation()
                startLatitude = locationViewModel.currentLocation?.latitude
                startLongitude = locationViewModel.currentLocation?.longitude
            } else if (abs(zRotationRate) < endThreshold && isDropStarted) {
                isDropStarted = false
                locationViewModel.getCurrentLocation()
                val endLatitude = locationViewModel.currentLocation?.latitude
                val endLongitude = locationViewModel.currentLocation?.longitude

                val timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date(System.currentTimeMillis())
                )

                if (startLatitude != null && startLongitude != null && endLatitude != null && endLongitude != null) {
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
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
