package com.danielchew.zwiftviewer.ui.ridedetail

import androidx.lifecycle.ViewModel
import com.danielchew.zwiftviewer.ZwiftPowerRide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RideDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RideDetailUiState())
    val uiState: StateFlow<RideDetailUiState> = _uiState

    fun loadRideById(rideId: String) {
        // For now, return a hardcoded ride
        val mockRide = ZwiftPowerRide(
            date = 1720000000000, // any mock timestamp
            zaid = "zaid123",
            title = "Mock Ride $rideId",
            zid = "zid456",
            elapsed = listOf(3600),
            distance = 25000,
            worldId = "1",
            sport = "cycling",
            fit = "fit789",
            aid = "aid999",
            avgSpeed = 38,
            avgHr = listOf(140),
            maxHr = listOf(180),
            avgCadence = listOf(85),
            calories = 600,
            avgPower = listOf(185),
            elevation = 300,
            zeid = 1234
        )
        _uiState.value = RideDetailUiState(ride = mockRide)
    }
}