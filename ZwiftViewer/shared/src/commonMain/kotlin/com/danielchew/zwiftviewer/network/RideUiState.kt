package com.danielchew.zwiftviewer.network

sealed interface RideUiState {
    object Loading : RideUiState
    data class Success(val rides: List<Ride>) : RideUiState
    data class Error(val message: String) : RideUiState
}