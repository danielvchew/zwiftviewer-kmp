package com.danielchew.zwiftviewer.ui.ridelist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RideListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        RideListUiState(
            rides = listOf(
                Ride("1", "Zwift Race - Volcano Circuit", "2025-06-01"),
                Ride("2", "Group Workout - FTP Builder", "2025-06-10")
            )
        )
    )
    val uiState: StateFlow<RideListUiState> = _uiState
}