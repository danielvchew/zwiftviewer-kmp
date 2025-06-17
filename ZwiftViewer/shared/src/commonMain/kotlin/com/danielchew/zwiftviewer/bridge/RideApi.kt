package com.danielchew.zwiftviewer.bridge

import com.danielchew.zwiftviewer.ZwiftPowerRide
import com.danielchew.zwiftviewer.network.KtorClientProvider.provideAuthenticatedClient
import com.danielchew.zwiftviewer.network.ZwiftPowerProfileApi
import kotlinx.coroutines.runBlocking
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("RideApiBridge", exact = true)
class RideApi {
    suspend fun getRides(profileId: String, cookies: Map<String, String>): List<ZwiftPowerRide> {
        val client = provideAuthenticatedClient(cookies)
        val api = ZwiftPowerProfileApi(client)

        return runBlocking {
            runCatching {
                api.getUserRideHistory(profileId, cookies)
            }.getOrDefault(emptyList())
        }
    }
}