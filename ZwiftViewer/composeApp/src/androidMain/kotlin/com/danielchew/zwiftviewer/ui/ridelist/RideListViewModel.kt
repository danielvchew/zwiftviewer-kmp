package com.danielchew.zwiftviewer.ui.ridelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielchew.zwiftviewer.RideStore
import com.danielchew.zwiftviewer.network.ZwiftPowerRideFetcher
import com.danielchew.zwiftviewer.utils.CookieStoreInstance
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RideListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RideListUiState(rides = emptyList()))
    val uiState: StateFlow<RideListUiState> = _uiState

    init {
        fetchRides()
    }

    private fun fetchRides() {
        viewModelScope.launch {
            try {
                val client = HttpClient(CIO)
                Log.d("ZwiftDebug", "RideListViewModel: extracting Zwift ID")
                //Legacy getID
                val zwiftId = CookieStoreInstance.legacyGetZwiftId() ?: throw Exception("Zwift ID not found in CookieStoreInstance")
                //WIll implement past MVP
                //val zwiftId = CookieStoreInstance.getZwiftId() ?: throw Exception("Zwift ID not found in CookieStoreInstance")
                val cookies = CookieStoreInstance.getCookies("zwiftpower.com")
                val cookieHeader = cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }
                Log.d("ZwiftDebug", "RideListViewModel: Zwift ID = $zwiftId | cookieHeader = $cookieHeader")
                val rides = ZwiftPowerRideFetcher.getUserRideHistory(zwiftId, cookieHeader)
                RideStore.rides = rides
                Log.d("ZwiftDebug", "RideListViewModel: fetched ${rides.size} rides")
                _uiState.value = RideListUiState(rides)
            } catch (e: Exception) {
                Log.d("ZwiftDebug", "RideListViewModel: error fetching rides - ${e.message}")
                _uiState.value = RideListUiState(emptyList())
            }
        }
    }
}