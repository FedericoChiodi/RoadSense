package com.sanpc.roadsense.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
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
    var potholeLevel by remember { mutableFloatStateOf(0f) }
    var gyroLevel by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, RoundedCornerShape(10.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    )
                ) {
                    Text(
                        text = if (isPotholeDetectionActive) "Stop Pothole Detection" else "Start Pothole Detection",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Text(text = "Pothole Level:", fontSize = 16.sp, color = Color.Black)
                Text(
                    text = potholeLevel.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, RoundedCornerShape(10.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    )
                ) {
                    Text(
                        text = if (isDropDetectionActive) "Stop Drop Detection" else "Start Drop Detection",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Text(text = "Gyroscope Level:", fontSize = 16.sp, color = Color.Black)
                Text(
                    text = gyroLevel.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }

    LaunchedEffect(potholeDetector) {
        scope.launch {
            potholeDetector.zValue.collect { value ->
                potholeLevel = value
            }
        }
    }

    LaunchedEffect(dropDetector) {
        scope.launch {
            dropDetector.gyroLevel.collect { value ->
                gyroLevel = value
            }
        }
    }
}
