package com.danielchew.zwiftviewer.ui.ridelist

import com.danielchew.zwiftviewer.network.Ride

data class RideListUiState(
    val rides: List<Ride>
)