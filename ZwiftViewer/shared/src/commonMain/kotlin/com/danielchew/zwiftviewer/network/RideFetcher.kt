package com.danielchew.zwiftviewer.network

interface RideFetcher {
    suspend fun getUserRideHistory(
        zwiftId: String,
        cookieHeader: String
    ): List<ZwiftPowerActivityResponse.DataItem>
}