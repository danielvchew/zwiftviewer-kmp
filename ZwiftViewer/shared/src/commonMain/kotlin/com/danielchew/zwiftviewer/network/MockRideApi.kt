package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.Ride

interface MockRideApi {
    suspend fun getAllRides(): List<Ride>
}