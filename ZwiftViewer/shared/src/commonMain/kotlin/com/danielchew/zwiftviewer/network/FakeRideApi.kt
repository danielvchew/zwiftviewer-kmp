package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.Ride

class FakeRideApi : RideApi {
    override suspend fun getAllRides(): List<Ride> {
        val mockRide = Ride(
            id = "ride_123",
            name = "Mock Crit",
            distance = 27300.0,
            duration = 3060
        )
        return listOf(mockRide)
    }
}