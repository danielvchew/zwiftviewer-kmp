package com.danielchew.zwiftviewer.ui.ridelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun RideListScreen(navController: NavController) {
    val viewModel: RideListViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ride List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        uiState.rides.forEach { ride ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("rideDetail/${ride.id}")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = ride.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = ride.date, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}