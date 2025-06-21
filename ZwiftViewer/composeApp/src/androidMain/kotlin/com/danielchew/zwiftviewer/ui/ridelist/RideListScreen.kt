package com.danielchew.zwiftviewer.ui.ridelist

import android.util.Log
import java.util.Locale

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun RideListScreen(navController: NavController) {
    val viewModel: RideListViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Ride List", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(uiState.rides) { ride ->
            val distanceMi = String.format(Locale.US, "%.1f mi", (ride.distance ?: 0.0) * 0.000621371)
            val elevationFt = String.format(Locale.US, "%.0f ft", (ride.elevation ?: 0.0).toDouble() * 3.28084)
            val elapsedSeconds = ride.elapsed?.toInt() ?: 0
            val hours = elapsedSeconds / 3600
            val minutes = (elapsedSeconds % 3600) / 60
            val elapsedTime = buildString {
                if (hours > 0) append("$hours h ")
                append("$minutes m")
            }

            val rideDateStr = ride.date?.toInt()?.let { timestamp ->
                val millis = timestamp * 1000L
                val sdf = java.text.SimpleDateFormat("MMM d, yyyy", Locale.US)
                sdf.format(java.util.Date(millis))
            } ?: "—"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        Log.d("ZwiftDebug", "Ride tapped: id=${ride.zaid}, title=${ride.title}")
                        navController.navigate("rideDetail/${ride.zaid}")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = rideDateStr, style = MaterialTheme.typography.bodySmall)
                    Text(text = ride.title ?: "Untitled Ride", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = distanceMi, modifier = Modifier.weight(1f))
                        Text(text = elevationFt, modifier = Modifier.weight(1f))
                        Text(text = elapsedTime, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}