package com.sanpc.roadsense.ui.screen

import android.content.Context
import androidx.compose.foundation.Canvas
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
import com.sanpc.roadsense.sensors.Thresholds
import com.sanpc.roadsense.ui.theme.LightGreen
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

    val potholeLevels = remember { mutableStateListOf<Float>() }
    val gyroLevels = remember { mutableStateListOf<Float>() }

    LaunchedEffect(potholeLevel) {
        if (potholeLevels.size > 50) potholeLevels.removeAt(0)
        potholeLevels.add(potholeLevel)
    }
    LaunchedEffect(gyroLevel) {
        if (gyroLevels.size > 50) gyroLevels.removeAt(0)
        gyroLevels.add(gyroLevel)
    }

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
                    if (greenBoxTimer) LightGreen else Color.LightGray,
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

                Text(text = "Pothole Level: $potholeLevel", fontSize = 16.sp, color = Color.Black)

                Graph(
                    data = potholeLevels,
                    lineColor = Color.Red,
                    minY = Thresholds.MIN_Y_POTHOLE,
                    maxY = Thresholds.MAX_Y_POTHOLE
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    if (greenBoxTimerDrop) LightGreen else Color.LightGray,
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
                        text = if (isDropDetectionActive) "Stop Slope Detection" else "Start Slope Detection",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Text(text = "Gyroscope Level: $gyroLevel", fontSize = 16.sp, color = Color.Black)

                Graph(
                    data = gyroLevels,
                    lineColor = Color.Blue,
                    thresholds = listOf(Thresholds.DROP_LOW, Thresholds.DROP_HIGH, -Thresholds.DROP_HIGH, -Thresholds.DROP_LOW),
                    minY = Thresholds.MIN_Y_DROP,
                    maxY = Thresholds.MAX_Y_DROP
                )
            }
        }
    }
}

@Composable
fun Graph(
    data: List<Float>,
    lineColor: Color,
    thresholds: List<Float> = emptyList(),
    minY: Float,
    maxY: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val rangeY = maxY - minY

        drawLine(
            color = Color.Gray,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Gray,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(0f, size.height),
            strokeWidth = 2f
        )

        val stepX = size.width / (data.size - 1).coerceAtLeast(1)
        val points = data.mapIndexed { index, value ->
            val x = stepX * index
            val y = size.height - ((value - minY) / rangeY) * size.height
            x to y
        }

        for (i in 0 until points.size - 1) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[i + 1]
            drawLine(
                color = lineColor,
                start = androidx.compose.ui.geometry.Offset(x1, y1),
                end = androidx.compose.ui.geometry.Offset(x2, y2),
                strokeWidth = 4f
            )
        }

        thresholds.forEach { threshold ->
            val y = size.height - ((threshold - minY) / rangeY) * size.height
            drawLine(
                color = Color.Red.copy(alpha = 0.7f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = 2f
            )
        }
    }
}

