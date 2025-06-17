package com.danielchew.zwiftviewer.viewmodel

import com.danielchew.zwiftviewer.network.RideUiState
import com.danielchew.zwiftviewer.network.ZwiftEventApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RideViewModel(
    private val api: ZwiftEventApi,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _state = MutableStateFlow<RideUiState>(RideUiState.Loading)
    val state: StateFlow<RideUiState> = _state

    fun loadRides() {
        _state.value = RideUiState.Loading
        scope.launch {
            try {
                val rides = api.getZwiftEvents()
                _state.value = RideUiState.Success(rides)
            } catch (e: Exception) {
                _state.value = RideUiState.Error("Failed to load rides: ${e.message}")
            }
        }
    }
}