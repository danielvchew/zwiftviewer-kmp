package com.danielchew.zwiftviewer.bridge

import com.danielchew.zwiftviewer.network.ZwiftEvent
import com.danielchew.zwiftviewer.network.ZwiftEventApi

class RideRepository(private val api: ZwiftEventApi) {
    suspend fun getRides(): List<ZwiftEvent> {
        return api.getZwiftEvents()
    }
}