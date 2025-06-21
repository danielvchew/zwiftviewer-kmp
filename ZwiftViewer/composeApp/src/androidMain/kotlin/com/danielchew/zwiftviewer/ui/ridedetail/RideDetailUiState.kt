package com.danielchew.zwiftviewer.ui.ridedetail

import com.danielchew.zwiftviewer.network.ZwiftPowerActivityResponse

data class RideDetailUiState(
    val ride: ZwiftPowerActivityResponse.DataItem? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)