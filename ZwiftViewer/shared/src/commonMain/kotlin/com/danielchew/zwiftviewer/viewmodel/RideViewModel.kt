package com.danielchew.zwiftviewer.viewmodel

import com.danielchew.zwiftviewer.bridge.RideApi
import com.danielchew.zwiftviewer.network.RideUiState
import com.danielchew.zwiftviewer.network.Ride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RideViewModel (
    private val api: RideApi,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _state = MutableStateFlow<RideUiState>(RideUiState.Loading)
    val state: StateFlow<RideUiState> = _state

    fun setRides(newRides: List<Ride>) {
        _state.value = RideUiState.Success(newRides)
    }

    fun loadRides(profileId: String, cookies: Map<String, String>) {
        println("ZwiftDebug: 🚴 Starting to load rides for profileId=$profileId")
        println("ZwiftDebug: 🍪 Cookies received: ${cookies.keys}")

        _state.value = RideUiState.Loading
        scope.launch {
            try {
                val rides = api.getUserRideHistory(profileId, cookies)
                println("ZwiftDebug: ✅ Loaded ${rides.size} rides.")
                _state.value = RideUiState.Success(rides)
            } catch (e: Exception) {
                println("ZwiftDebug: ❌ Failed to load rides: ${e.message}")
                _state.value = RideUiState.Error("Failed to load rides: ${e.message}")
            }
        }
    }
}