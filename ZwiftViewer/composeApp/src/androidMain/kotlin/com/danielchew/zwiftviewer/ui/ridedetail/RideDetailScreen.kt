package com.danielchew.zwiftviewer.ui.ridedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danielchew.zwiftviewer.ui.components.RideCard

@Composable
fun RideDetailScreen(rideId: String) {
    val viewModel: RideDetailViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(rideId) {
        viewModel.loadRideById(rideId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ride Detail", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        uiState.ride?.let {
            RideCard(ride = it)
        } ?: Text("Loading ride...")
    }
}