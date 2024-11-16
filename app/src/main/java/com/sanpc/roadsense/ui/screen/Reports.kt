package com.sanpc.roadsense.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.theme.LightGreen
import com.sanpc.roadsense.ui.theme.LightRed
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import java.util.Locale

@Composable
fun Reports(
    potholes: List<Pothole>,
    drops: List<Drop>
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Potholes", "Drops")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
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
    val cardBackgroundColor = if (report.syncStatus) LightGreen else LightRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Time: ${report.detectionDate}", fontSize = 16.sp)
            Text(text = "Latitude: ${report.latitude.formatCoordinate()}", fontSize = 16.sp)
            Text(text = "Longitude: ${report.longitude.formatCoordinate()}", fontSize = 16.sp)
        }
    }
}

@Composable
fun ReportDrop(report: Drop) {
    val cardBackgroundColor = if (report.syncStatus) LightGreen else LightRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Time: ${report.detectionDate}", fontSize = 16.sp)
            Text(text = "S_lat: ${report.startLatitude.formatCoordinate()}", fontSize = 16.sp)
            Text(text = "S_lon: ${report.startLongitude.formatCoordinate()}", fontSize = 16.sp)
            Text(text = "E_lat: ${report.endLatitude.formatCoordinate()}", fontSize = 16.sp)
            Text(text = "E_lon: ${report.endLongitude.formatCoordinate()}", fontSize = 16.sp)
        }
    }
}

private fun Double.formatCoordinate(): String = String.format(Locale.getDefault(), "%.7f", this)

@Preview
@Composable
fun ReportsPreview() {
    RoadSenseTheme {
    }
}

