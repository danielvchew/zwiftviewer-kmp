package com.danielchew.zwiftviewer.ui.ridedetail

import androidx.lifecycle.ViewModel
import android.util.Log
import com.danielchew.zwiftviewer.RideStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RideDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RideDetailUiState())
    val uiState: StateFlow<RideDetailUiState> = _uiState

    fun loadRideById(rideId: String) {
        Log.d("ZwiftDebug", "RideDetailViewModel: looking for rideId=$rideId")
        val ride = RideStore.rides.find { it.zaid == rideId }
        Log.d("ZwiftDebug", "Ride found: ${ride?.title ?: "NOT FOUND"}")
        _uiState.value = RideDetailUiState(ride = ride)
    }
}