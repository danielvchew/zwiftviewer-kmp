package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.ZwiftPowerRide

sealed interface RideUiState {
    object Loading : RideUiState
    data class Success(val rides: List<ZwiftPowerActivityResponse.DataItem>) : RideUiState
    data class Error(val message: String) : RideUiState
}