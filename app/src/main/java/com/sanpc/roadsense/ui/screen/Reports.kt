package com.sanpc.roadsense.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme

data class Pothole(
    val timestamp: String,
    val latitude: Double,
    val longitude: Double
)

data class Drop(
    val timestamp: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double
)

val samplePotholes = listOf(
    Pothole("2024-11-12 08:45", 37.7749, -122.4194),
    Pothole("2024-11-12 09:15", 37.8044, -122.2711)
)

val sampleDrops = listOf(
    Drop("2024-11-12 08:30", 37.7749, -122.4194, 37.8044, -122.2711),
    Drop("2024-11-12 09:00", 37.8044, -122.2711, 37.7749, -122.4194)
)

@Composable
fun Reports() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Potholes", "Drops")

    val potholes = samplePotholes
    val drops = sampleDrops

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Reports", fontSize = 30.sp, color = Orange, modifier = Modifier.padding(bottom = 16.dp))

            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Orange,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Orange
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) Orange else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (selectedTabIndex == 0) {
                    items(potholes) { pothole ->
                        ReportPothole(report = pothole)
                    }
                } else {
                    items(drops) { drop ->
                        ReportDrop(report = drop)
                    }
                }
            }
        }
    }
}


@Composable
fun ReportPothole(report: Pothole) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Time: ${report.timestamp}", fontSize = 16.sp)
            Text(text = "Latitude: ${report.latitude}", fontSize = 16.sp)
            Text(text = "Longitude: ${report.longitude}", fontSize = 16.sp)
        }
    }
}

@Composable
fun ReportDrop(report: Drop) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Time: ${report.timestamp}", fontSize = 16.sp)
            Text(text = "Latitude: ${report.startLatitude} -> ${report.endLatitude}", fontSize = 16.sp)
            Text(text = "Longitude: ${report.startLongitude} -> ${report.endLongitude}", fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
fun ReportsPreview() {
    RoadSenseTheme {
        Reports()
    }
}
