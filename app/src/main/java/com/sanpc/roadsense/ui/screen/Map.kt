package com.sanpc.roadsense.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.theme.Orange
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@Composable
fun Map(
    context: Context,
    potholes: List<Pothole>,
    drops: List<Drop>
) {
    val potholeIcon: Drawable? = context.getDrawable(R.drawable.pothole_icon)
    val dropStartIcon: Drawable? = context.getDrawable(R.drawable.drop_start_icon)
    val dropEndIcon: Drawable? = context.getDrawable(R.drawable.drop_end_icon)

    val currentContext = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                Log.d("Map", "Location permission granted")
            } else {
                Log.d("Map", "Location permission denied")
            }
        }
    )

    var isLocationCentered by remember { mutableStateOf(false) }
    val locationManager = currentContext.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(
                currentContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                try {
                    val location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)
                    location?.let {
                        isLocationCentered = true
                    }
                } catch (e: SecurityException) {
                    Log.e("Map", "Permission not granted for location")
                }
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLocationCentered) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = { context ->
                    val mapView = MapView(context)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setMultiTouchControls(true)
                    mapView.controller.setZoom(16.20)
                    mapView.isTilesScaledToDpi = false
                    mapView.controller.setCenter(locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER).let { GeoPoint(it!!.latitude, it.longitude) })

                    val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
                    mLocationOverlay.enableMyLocation()
                    mLocationOverlay.enableFollowLocation()
                    mLocationOverlay.isDrawAccuracyEnabled = true
                    mapView.overlays.add(mLocationOverlay)

                    drops.forEach { drop ->
                        val startGeoPoint = GeoPoint(drop.startLatitude, drop.startLongitude)
                        val endGeoPoint = GeoPoint(drop.endLatitude, drop.endLongitude)

                        val startMarker = Marker(mapView)
                        startMarker.position = startGeoPoint
                        startMarker.icon = dropStartIcon
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        startMarker.setOnMarkerClickListener { _, _ ->
                            startMarker.title = "Drop Start"
                            startMarker.snippet = "Start Location: ${startGeoPoint.latitude}, ${startGeoPoint.longitude}<br>Detection Date: ${drop.detectionDate}"
                            startMarker.showInfoWindow()
                            true
                        }

                        val endMarker = Marker(mapView)
                        endMarker.position = endGeoPoint
                        endMarker.icon = dropEndIcon
                        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        endMarker.setOnMarkerClickListener { _, _ ->
                            endMarker.title = "Drop End"
                            endMarker.snippet = "End Location: ${endGeoPoint.latitude}, ${endGeoPoint.longitude}<br>Detection Date: ${drop.detectionDate}"
                            endMarker.showInfoWindow()
                            true
                        }

                        val line = Polyline()
                        line.addPoint(startGeoPoint)
                        line.addPoint(endGeoPoint)
                        line.outlinePaint.strokeWidth = 7f
                        line.outlinePaint.color = context.getColor(R.color.green)
                        mapView.overlays.add(line)

                        mapView.overlays.add(startMarker)
                        mapView.overlays.add(endMarker)
                    }

                    potholes.forEach { pothole ->
                        val geoPoint = GeoPoint(pothole.latitude, pothole.longitude)
                        val marker = Marker(mapView)
                        marker.position = geoPoint
                        marker.icon = potholeIcon
                        marker.setOnMarkerClickListener { _, _ ->
                            marker.title = "Pothole"
                            marker.snippet = "Location: ${geoPoint.latitude}, ${geoPoint.longitude}<br>Detection Date: ${pothole.detectionDate}"
                            marker.showInfoWindow()
                            true
                        }
                        mapView.overlays.add(marker)
                    }

                    mapView
                }
            )
        } else {
            Text(
                text = "Loading Map...",
                color = Orange,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}


