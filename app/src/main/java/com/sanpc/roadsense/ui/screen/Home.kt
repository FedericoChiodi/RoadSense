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
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel
import kotlinx.coroutines.delay

@Composable
fun Home(
    context: Context,
    potholeViewModel: PotholeViewModel,
    dropViewModel: DropViewModel,
    locationViewModel: LocationViewModel
) {
    rememberCoroutineScope()

    val potholeDetector = remember { PotholeDetector(context, locationViewModel) }
    val dropDetector = remember { DropDetector(context, locationViewModel) }

    var isPotholeDetectionActive by remember { mutableStateOf(false) }
    var isDropDetectionActive by remember { mutableStateOf(false) }

    var potholeDetected by remember { mutableStateOf(false) }
    var dropDetected by remember { mutableStateOf(false) }

    var greenBoxTimer by remember { mutableStateOf(false) }
    var greenBoxTimerDrop by remember { mutableStateOf(false) }

    val potholeData = potholeDetector.potholeData.collectAsState(initial = null)
    val dropData = dropDetector.dropData.collectAsState(initial = null)

    val potholeLevel by potholeDetector.zValue.collectAsState(initial = 0f)
    val gyroLevel by dropDetector.gyroLevel.collectAsState(initial = 0f)


    LaunchedEffect(potholeData.value) {
        potholeData.value?.let {
            potholeViewModel.insert(potholeData.value!!)
            potholeDetected = true
            greenBoxTimer = true
            delay(1250)
            greenBoxTimer = false
        }
    }

    LaunchedEffect(dropData.value) {
        dropData.value?.let {
            dropViewModel.insert(dropData.value!!)
            dropDetected = true
            greenBoxTimerDrop = true
            delay(1250)
            greenBoxTimerDrop = false
        }
    }

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
                .background(
                    if (greenBoxTimer) Color.Green else Color.LightGray,
                    RoundedCornerShape(10.dp)
                )
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
                .background(
                    if (greenBoxTimerDrop) Color.Green else Color.LightGray,
                    RoundedCornerShape(10.dp)
                )
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
}

