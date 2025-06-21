package com.danielchew.zwiftviewer.bridge

import com.danielchew.zwiftviewer.network.ZwiftPowerActivityResponse
import com.danielchew.zwiftviewer.network.ZwiftPowerRideFetcher
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("RideApiBridge", exact = true)
class RideApi {

    suspend fun getUserRideHistory(zwiftId: String, cookies: Map<String, String>): List<ZwiftPowerActivityResponse.DataItem> {
        println("ZwiftDebug: getUserRideHistory received cookies: ${cookies.keys}")

        val cookieHeader = cookies
            .filterValues { !it.isNullOrBlank() }
            .map { "${it.key}=${it.value}" }
            .joinToString("; ")

        println("ZwiftDebug: 🍪 Assembled Cookie Header: $cookieHeader")

        return try {
            ZwiftPowerRideFetcher.getUserRideHistory(zwiftId, cookieHeader)
        } catch (e: Exception) {
            println("ZwiftDebug: RideApi Error: ${e.message}")
            emptyList()
        }
    }
}