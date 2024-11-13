package com.sanpc.roadsense.ui.screen

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sanpc.roadsense.sensors.PotholeDetector
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(
    context: Context,
    locationViewModel: LocationViewModel
) {
    val potholeDetector = remember { PotholeDetector(context, locationViewModel) }
    var isRecording by remember { mutableStateOf(false) }
    val potholeDataList = remember { mutableStateListOf<Double>() }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(true) {
        locationPermissions.launchMultiplePermissionRequest()
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            potholeDetector.potholeData.collect { pothole ->
                potholeDataList.apply {
                    clear()
                    add(pothole.latitude)
                    add(pothole.longitude)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                isRecording = !isRecording
                if (isRecording) {
                    potholeDetector.startDetection()
                } else {
                    potholeDetector.stopDetection()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange
            )
        ) {
            Text(text = if (isRecording) "Stop Recording" else "Start Recording")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Measures:")
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(potholeDataList.size) { index ->
                Text(text = potholeDataList[index].toString())
            }
        }
    }
}


@Preview
@Composable
fun HomePreview(){
    RoadSenseTheme {
    }
}
