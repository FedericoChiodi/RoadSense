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
    private val startThreshold: Float = Thresholds.DROP_HIGH,
    private val endThreshold: Float = Thresholds.DROP_LOW
) : SensorEventListener {

    private val _dropData = MutableSharedFlow<Drop>(replay = 1)
    val dropData = _dropData.asSharedFlow()

    private val _gyroLevel = MutableSharedFlow<Float>(replay = 1)
    val gyroLevel = _gyroLevel.asSharedFlow()

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val username: String = UserPreferences(context).username

    private var isDropStarted = false
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null

    fun startDetection() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: Log.e("DropDetector", "Accelerometer not available")
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

            _gyroLevel.tryEmit(angle.toFloat())

            if (abs(angle) > startThreshold && !isDropStarted) {
                isDropStarted = true
                retrieveLocation { latitude, longitude ->
                    if (latitude != null && longitude != null) {
                        startLatitude = latitude
                        startLongitude = longitude
                    } else {
                        isDropStarted = false
                    }
                }
            } else if (abs(angle) < endThreshold && isDropStarted) {
                isDropStarted = false
                retrieveLocation { latitude, longitude ->
                    if (latitude != null && longitude != null && startLatitude != null && startLongitude != null) {
                        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(System.currentTimeMillis())

                        val dropEvent = Drop(
                            username = username,
                            startLatitude = startLatitude!!,
                            startLongitude = startLongitude!!,
                            endLatitude = latitude,
                            endLongitude = longitude,
                            detectionDate = timestamp,
                            syncStatus = false
                        )
                        _dropData.tryEmit(dropEvent)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun retrieveLocation(callback: (Double?, Double?) -> Unit) {
        locationViewModel.getCurrentLocation()
        val location = locationViewModel.currentLocation
        callback(location?.latitude, location?.longitude)
    }
}
