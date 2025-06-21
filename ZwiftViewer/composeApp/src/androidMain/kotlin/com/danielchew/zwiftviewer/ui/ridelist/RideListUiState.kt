package com.danielchew.zwiftviewer.ui.ridelist

import com.danielchew.zwiftviewer.network.ZwiftPowerActivityResponse

data class RideListUiState(
    val rides: List<ZwiftPowerActivityResponse.DataItem>
)