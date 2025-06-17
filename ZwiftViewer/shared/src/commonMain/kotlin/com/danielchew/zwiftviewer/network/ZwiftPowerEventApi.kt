package com.danielchew.zwiftviewer.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders

class ZwiftPowerEventApi(private val client: HttpClient) : ZwiftEventApi {
    override suspend fun getZwiftEvents(): List<ZwiftEvent> {
        return client.get("https://zwiftpower.com/cache3/list3p_zp/zp_event_list_3.json").body()
    }
}