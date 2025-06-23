package com.danielchew.zwiftviewer.ui.ridedetail

import com.danielchew.zwiftviewer.network.Ride

data class RideDetailUiState(
    val ride: Ride? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)