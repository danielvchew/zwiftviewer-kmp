package com.danielchew.zwiftviewer.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideAuthenticatedClient(cookies: Map<String, String>): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            header("accept", "application/json, text/javascript, */*; q=0.01")
            header("accept-language", "en-US,en;q=0.9")
            header("referer", "https://zwiftpower.com/profile.php?z=2109820")
            header("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"")
            header("sec-ch-ua-mobile", "?0")
            header("sec-ch-ua-platform", "\"macOS\"")
            header("sec-fetch-dest", "empty")
            header("sec-fetch-mode", "cors")
            header("sec-fetch-site", "same-origin")
            header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36")
            header("x-requested-with", "XMLHttpRequest")

            // Cookie header from your app’s cookie map
            val cookieHeader = cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }
            header("cookie", cookieHeader)
        }

        install(io.ktor.client.plugins.cookies.HttpCookies) {
            storage = object : io.ktor.client.plugins.cookies.CookiesStorage {
                override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {}
                override suspend fun get(requestUrl: Url): List<Cookie> {
                    return cookies.map { (name, value) ->
                        Cookie(name = name, value = value)
                    }
                }
                override fun close() {}
            }
        }
    }
}