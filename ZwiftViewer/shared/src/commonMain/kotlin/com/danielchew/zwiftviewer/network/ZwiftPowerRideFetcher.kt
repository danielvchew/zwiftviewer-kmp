package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.utils.getCurrentTimeMillis
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class ZwiftPowerRideFetcher(private val client: HttpClient) : RideFetcher {

    override suspend fun getUserRideHistory(
        zwiftId: String,
        cookieHeader: String
    ): List<ZwiftPowerActivityResponse.DataItem> {
        return try {
            val timestamp = getCurrentTimeMillis()
            val url = "https://zwiftpower.com/api3.php?do=activities&z=$zwiftId&_=$timestamp"
            println("ZwiftDebug: 🔗 Requesting $url")

            val response: HttpResponse = client.get(url) {
                println("ZwiftDebug: 🧾 Sending headers with User-Agent and full cookies")
                println("ZwiftDebug: 🍪 Final Cookie Header: $cookieHeader")
                headers {
                    append(HttpHeaders.Accept, "application/json, text/javascript, */*; q=0.01")
                    append(HttpHeaders.Referrer, "https://zwiftpower.com/profile.php?z=$zwiftId")
                    append("x-requested-with", "XMLHttpRequest")
                    append(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Mobile Safari/537.36")
                    append(HttpHeaders.Cookie, cookieHeader)
                }
            }

            println("ZwiftDebug: 🟦 Status Code: ${response.status}")
            val rawBody = response.bodyAsText()
            println("ZwiftDebug: 🟨 RAW BODY (first 500 chars):\n${rawBody.take(500)}")

            val json = Json { ignoreUnknownKeys = true }

            val parsed = try {
                json.decodeFromString<ZwiftPowerActivityResponse>(rawBody)
            } catch (e: Exception) {
                println("ZwiftDebug: 🟥 PARSE ERROR: ${e.message}")
                throw e
            }

            return parsed.data

        } catch (e: Exception) {
            println("ZwiftDebug: 🟥 Error fetching rides: ${e.message}")
            emptyList()
        }
    }
}