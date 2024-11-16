package com.sanpc.roadsense.ui.viewmodel

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanpc.roadsense.utils.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel() {

    var currentLocation by mutableStateOf<Location?>(null)
        private set

    var locationGeopoint by mutableStateOf<GeoPoint?>(null)
        private set

    fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val location = locationTracker.getCurrentLocation()

            location?.let {
                currentLocation = it
                locationGeopoint = GeoPoint(it.latitude, it.longitude)
            }
        }
    }
}
