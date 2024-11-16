package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun Map(
    context: Context,
    potholes: List<Pothole>,
    drops: List<Drop>,
    locationViewModel: LocationViewModel
) {
    val potholeIcon: Drawable? = context.getDrawable(R.drawable.pothole_icon)
    val userIcon: Drawable? = context.getDrawable(R.drawable.user_icon)
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }

    LaunchedEffect(Unit) {
        locationViewModel.getCurrentLocation()
    }

    LaunchedEffect(locationViewModel.locationGeopoint) {
        userLocation = locationViewModel.locationGeopoint
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (userLocation != null) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = { context ->
                    val mapView = MapView(context)
                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setMultiTouchControls(true)
                    mapView.controller.setCenter(userLocation)
                    mapView.controller.setZoom(10.75)
                    mapView.isTilesScaledToDpi = true

                    drops.forEach { drop ->
                        val startGeoPoint = GeoPoint(drop.startLatitude, drop.startLongitude)
                        val endGeoPoint = GeoPoint(drop.endLatitude, drop.endLongitude)

                        val startMarker = Marker(mapView)
                        startMarker.position = startGeoPoint
                        startMarker.setOnMarkerClickListener { _, _ ->
                            startMarker.title = "Drop Start"
                            startMarker.snippet = "Start Location: ${startGeoPoint.latitude}, ${startGeoPoint.longitude}<br>Detection Date: ${drop.detectionDate}"
                            startMarker.showInfoWindow()
                            true
                        }

                        val endMarker = Marker(mapView)
                        endMarker.position = endGeoPoint
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
                        line.outlinePaint.color = context.getColor(R.color.black)
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

                    userLocation?.let { userLocation ->
                        val userMarker = Marker(mapView).apply {
                            position = userLocation
                            icon = userIcon
                            setOnMarkerClickListener { _, _ ->
                                title = "User"
                                snippet = "Location: ${userLocation.latitude}, ${userLocation.longitude}"
                                showInfoWindow()
                                true
                            }
                        }
                        mapView.overlays.add(userMarker)
                    }

                    mapView
                },
                update = { view ->
                    userLocation?.let { geoPoint ->
                        view.controller.setCenter(geoPoint)
                    }
                }
            )
        }
    }
}
