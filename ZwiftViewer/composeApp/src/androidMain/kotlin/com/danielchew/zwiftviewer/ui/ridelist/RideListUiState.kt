package com.danielchew.zwiftviewer.ui.ridelist

data class Ride(
    val id: String,
    val name: String,
    val date: String
)

data class RideListUiState(
    val rides: List<Ride> = emptyList()
)