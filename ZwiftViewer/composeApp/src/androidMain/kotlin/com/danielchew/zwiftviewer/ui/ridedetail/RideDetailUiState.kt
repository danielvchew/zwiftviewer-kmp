package com.danielchew.zwiftviewer.ui.ridedetail

import com.danielchew.zwiftviewer.ZwiftPowerRide

data class RideDetailUiState(
    val ride: ZwiftPowerRide? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)