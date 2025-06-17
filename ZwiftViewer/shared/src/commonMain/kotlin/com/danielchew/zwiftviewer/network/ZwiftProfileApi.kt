package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.ZwiftPowerRide

interface ZwiftProfileApi {
    suspend fun getUserRideHistory(profileId: String, cookies: Map<String, String>): List<ZwiftPowerRide>
}