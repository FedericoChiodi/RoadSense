package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanpc.roadsense.sensors.PotholeDetector
import com.sanpc.roadsense.sensors.DropDetector
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.ui.theme.Orange
import kotlinx.coroutines.launch

@Composable
fun Home(
    context: Context,
    locationViewModel: LocationViewModel
) {
    val scope = rememberCoroutineScope()
    val potholeDetector = remember { PotholeDetector(context, locationViewModel) }
    val dropDetector = remember { DropDetector(context, locationViewModel) }

    var isPotholeDetectionActive by remember { mutableStateOf(false) }
    var isDropDetectionActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (isPotholeDetectionActive) {
                    potholeDetector.stopDetection()
                } else {
                    potholeDetector.startDetection()
                }
                isPotholeDetectionActive = !isPotholeDetectionActive
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPotholeDetectionActive) Orange.copy(alpha = 0.85f) else Orange
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = if (isPotholeDetectionActive) "Stop Pothole Detection" else "Start Pothole Detection",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Spacer(
            modifier = Modifier.padding(bottom = 26.dp)
        )

        Button(
            onClick = {
                if (isDropDetectionActive) {
                    dropDetector.stopDetection()
                } else {
                    dropDetector.startDetection()
                }
                isDropDetectionActive = !isDropDetectionActive
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDropDetectionActive) Orange.copy(alpha = 0.85f) else Orange
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = if (isDropDetectionActive) "Stop Drop Detection" else "Start Drop Detection",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        LaunchedEffect(potholeDetector) {
            scope.launch {
                potholeDetector.potholeData.collect {
                    Toast.makeText(context, "Pothole Detected!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        LaunchedEffect(dropDetector) {
            scope.launch {
                dropDetector.dropData.collect {
                    Toast.makeText(context, "Drop Detected!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
