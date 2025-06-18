package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.ZwiftPowerRide
import com.danielchew.zwiftviewer.utils.getCurrentTimeMillis
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.cookies
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class ZwiftPowerProfileApi(private val client: HttpClient) : ZwiftProfileApi {
    override suspend fun getUserRideHistory(profileId: String, cookies: Map<String, String>): List<ZwiftPowerRide> {
        return try {
            val cookieHeader = cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

            val response: String = client.get("https://zwiftpower.com/api3.php") {
                url {
                    parameters.append("do", "activities")
                    parameters.append("z", profileId)
                    parameters.append("_", getCurrentTimeMillis().toString())
                }
                header("Accept", "application/json, text/javascript, */*; q=0.01")
                header("User-Agent", "<use same one as curl>")
                header("Referer", "https://zwiftpower.com/profile.php?z=$profileId")
                header("X-Requested-With", "XMLHttpRequest")
                header("Cookie", cookieHeader)
            }.bodyAsText()

            println("ZwiftDebug: Raw response: $response")

            val jsonElement = Json.parseToJsonElement(response)
            println("ZwiftDebug: JSON Element = $jsonElement")

            val jsonData = jsonElement.jsonObject["data"]
            println("ZwiftDebug: data field = $jsonData")

            val rideList = jsonData?.let {
                try {
                    Json.decodeFromJsonElement<List<ZwiftPowerRide>>(it)
                } catch (e: Exception) {
                    println("ZwiftDebug: Failed to decode data array: ${e.message}")
                    emptyList()
                }
            } ?: emptyList()
            rideList.reversed()
        } catch (e: Exception) {
            println("ZwiftDebug: Failed to load rides: ${e.message}")
            emptyList()
        }
    }
}