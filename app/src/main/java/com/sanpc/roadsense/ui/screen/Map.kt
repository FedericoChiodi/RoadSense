package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.Polyline
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@Composable
fun Map(context: Context, potholes: List<Pothole>, drops: List<Drop>) {
    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }

    val potholeIcon: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.pothole_icon))
    }
    val dropIconStart: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.drop_icon_start))
    }
    val dropIconEnd: Drawable? by remember {
        mutableStateOf(context.getDrawable(R.drawable.drop_icon_end))
    }

    SideEffect {
        mapProperties = mapProperties
            .copy(isTilesScaledToDpi = true)
            .copy(tileSources = TileSourceFactory.MAPNIK)
            .copy(isEnableRotationGesture = true)
            .copy(zoomButtonVisibility = ZoomButtonVisibility.ALWAYS)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(44.92875, 11.34106)
            zoom = 9.45
        }

        OpenStreetMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
            properties = mapProperties
        ){
            potholes.forEach {
                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(it.latitude, it.longitude)),
                    icon = potholeIcon,
                    title = "Pothole",
                    snippet = it.latitude.toString() + " - " + it.longitude.toString() + " @ " + it.detectionDate
                ){
                    Column(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.title)
                        Text(text = it.snippet, fontSize = 10.sp)
                    }
                }
            }

            drops.forEach {
                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(it.startLatitude, it.startLongitude)),
                    icon = dropIconStart,
                    title = "Drop start",
                    snippet = it.startLatitude.toString() + " - " + it.startLongitude.toString() + " @ " + it.detectionDate
                ){
                    Column(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.title)
                        Text(text = it.snippet, fontSize = 10.sp)
                    }
                }
                Marker(
                    state = rememberMarkerState(geoPoint = GeoPoint(it.endLatitude, it.endLongitude)),
                    icon = dropIconEnd,
                    title = "Drop end",
                    snippet = it.endLatitude.toString() + " - " + it.endLongitude.toString() + " @ " + it.detectionDate
                ){
                    Column(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.title)
                        Text(text = it.snippet, fontSize = 10.sp)
                    }
                }
                Polyline(
                    geoPoints = listOf(
                        GeoPoint(it.startLatitude, it.startLongitude),
                        GeoPoint(it.endLatitude, it.endLongitude)
                    ),
                    color = Color.Black,
                    width = 10f
                )
            }

        }
    }
}


@Preview
@Composable
fun MapPreview(){
    RoadSenseTheme {
    }
}