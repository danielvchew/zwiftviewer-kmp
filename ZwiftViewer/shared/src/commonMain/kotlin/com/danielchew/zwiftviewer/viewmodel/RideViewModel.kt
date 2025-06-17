package com.danielchew.zwiftviewer.viewmodel

import com.danielchew.zwiftviewer.network.RideUiState
import com.danielchew.zwiftviewer.network.ZwiftEventApi
import com.danielchew.zwiftviewer.network.ZwiftProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RideViewModel(
    private val api: ZwiftProfileApi,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _state = MutableStateFlow<RideUiState>(RideUiState.Loading)
    val state: StateFlow<RideUiState> = _state

    fun loadRides(profileId: String, cookies: Map<String, String>) {
        _state.value = RideUiState.Loading
        scope.launch {
            try {
                val rides = api.getUserRideHistory(profileId, cookies)
                _state.value = RideUiState.Success(rides)
            } catch (e: Exception) {
                println("ZwiftDebug: Failed to load rides: ${e.message}")
                _state.value = RideUiState.Error("Failed to load rides: ${e.message}")
            }
        }
    }
}