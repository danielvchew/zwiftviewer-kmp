package com.danielchew.zwiftviewer.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielchew.zwiftviewer.network.Ride
import com.danielchew.zwiftviewer.viewmodel.StatWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Helper to safely extract StatWrapper label and fallback
fun statLabelOrDash(stat: StatWrapper?): String {
    val raw = stat?.label()
    Log.d("ZwiftDebug", "statLabelOrDash: class=${stat?.javaClass?.simpleName}, raw=$raw")
    val value = raw?.takeIf { it.isNotBlank() }
    return value ?: "—"
}

fun formatUnixDate(seconds: Long): String {
    val date = Date(seconds * 1000)
    val format = SimpleDateFormat("MMM dd yyyy", Locale.US)
    return format.format(date)
}

fun formatElapsed(seconds: Int): String {
    val minutes = (seconds / 60) % 60
    val hours = seconds / 3600
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}

@Composable
fun RideCard(ride: Ride) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Debug logs
        Log.d("ZwiftDebug", "RideCard: title=${ride.title}, date=${ride.date}, distance=${ride.distance}, elevation=${ride.elevation}")

        val date = ride.date?.toInt()?.let { formatUnixDate(it.toLong()) } ?: "—"
        val sport = ride.sport ?: "—"
        val elapsed = ride.elapsed?.toInt()?.let { formatElapsed(it) } ?: "—"
        val distanceMi = ride.distance?.let { String.format(Locale.US, "%.1f", it * 0.000621371) } ?: "—"
        val elevationFt = ride.elevation?.let { String.format(Locale.US, "%.0f", it * 3.28084) } ?: "—"
        val avgSpeed = ride.avgSpeed?.let { String.format(Locale.US, "%.2f km/h", it) } ?: "—"
        val avgHr = statLabelOrDash(ride.avgHr)
        val maxHr = statLabelOrDash(ride.maxHr)
        val avgPower = statLabelOrDash(ride.avgPower)
        val avgCadence = statLabelOrDash(ride.avgCadence)
        val calories = ride.calories?.takeIf { it != 0 }?.toString() ?: "—"

        // Debug: resolved values
        Log.d("ZwiftDebug", "RideCard resolved: date=$date, sport=$sport, elapsed=$elapsed, distanceMi=$distanceMi, elevationFt=$elevationFt, avgSpeed=$avgSpeed, avgHr=$avgHr, maxHr=$maxHr, avgPower=$avgPower, avgCadence=$avgCadence, calories=$calories")

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = ride.title ?: "Untitled Ride", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📅 Date: $date")
            Text("🏃 Sport: $sport")
            Text("🕒 Elapsed: $elapsed")
            Text("📏 Distance: $distanceMi mi")
            Text("🧗 Elevation: $elevationFt ft")
            Text("🚴 Avg Speed: $avgSpeed")
            Text("💓 Avg HR: $avgHr bpm")
            Text("🔺 Max HR: $maxHr bpm")
            Text("⚡ Avg Power: $avgPower W")
            Text("🔁 Avg Cadence: $avgCadence rpm")
            Text("🔥 Calories: $calories kcal")
        }
    }
}