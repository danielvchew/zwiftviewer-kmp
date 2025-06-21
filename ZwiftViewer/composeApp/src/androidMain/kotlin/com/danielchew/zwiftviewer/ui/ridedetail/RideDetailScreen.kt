package com.danielchew.zwiftviewer.ui.ridedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danielchew.zwiftviewer.ui.components.RideCard
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideDetailScreen(rideId: String) {
    val viewModel: RideDetailViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Log.d("ZwiftDebug", "Displaying RideDetailScreen for rideId=$rideId")

    LaunchedEffect(rideId) {
        viewModel.loadRideById(rideId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ride Details") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val ride = uiState.ride
            if (ride != null) {
                Log.d("ZwiftDebug", "Ride detail data: avgHr=${ride.avgHr}, avgPower=${ride.avgPower}, avgCadence=${ride.avgCadence}, date=${ride.date}")
                RideCard(ride = ride)
            } else {
                Text("Loading ride...")
            }
        }
    }
}
