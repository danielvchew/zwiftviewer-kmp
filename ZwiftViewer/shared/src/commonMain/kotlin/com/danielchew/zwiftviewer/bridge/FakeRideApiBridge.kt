package com.danielchew.zwiftviewer.bridge

import com.danielchew.zwiftviewer.Ride
import com.danielchew.zwiftviewer.network.FakeRideApi

class FakeRideApiBridge {
    private val api = FakeRideApi()

    suspend fun load(): List<Ride> {
        return api.getAllRides()
    }
}