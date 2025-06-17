package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.Ride

interface RideApi {
    suspend fun getAllRides(): List<Ride>
}