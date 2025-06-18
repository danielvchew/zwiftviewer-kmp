package com.danielchew.zwiftviewer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielchew.zwiftviewer.ZwiftPowerRide

@Composable
fun RideCard(ride: ZwiftPowerRide) {
    val distanceKm = ride.distance.div(1000.0).let { String.format("%.1f", it) }
    val avgPower = ride.avgPower.firstOrNull()?.toString() ?: "N/A"

    Column(modifier = Modifier.padding(16.dp)) {
        Text("🏁 ${ride.title}")
        Text("Distance: $distanceKm km")
        Text("Avg Power: $avgPower W")
    }
}