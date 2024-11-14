package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun Map(context: Context, potholes: List<Pothole>, drops: List<Drop>) {
    val potholeIcon: Drawable? = context.getDrawable(R.drawable.pothole_icon)

    val center = GeoPoint(44.8385, 11.5195)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.controller.setCenter(center)
            mapView.controller.setZoom(10.75)
            mapView.isTilesScaledToDpi = true

            drops.forEach { drop ->
                val startGeoPoint = GeoPoint(drop.startLatitude, drop.startLongitude)
                val endGeoPoint = GeoPoint(drop.endLatitude, drop.endLongitude)

                val startMarker = Marker(mapView)
                startMarker.position = startGeoPoint
                //startMarker.icon = dropIconStart
                startMarker.setOnMarkerClickListener { _, _ ->
                    startMarker.title = "Drop Start"
                    startMarker.snippet = "Start Location: ${startGeoPoint.latitude}, ${startGeoPoint.longitude}<br>Detection Date: ${drop.detectionDate}"
                    startMarker.showInfoWindow()
                    true
                }

                val endMarker = Marker(mapView)
                endMarker.position = endGeoPoint
                //endMarker.icon = dropIconEnd
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

            mapView
        },
        update = { view ->
            view.controller.setCenter(center)
            view.controller.setZoom(10.75)
        }
    )
}
