package com.danielchew.zwiftviewer.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideAuthenticatedClient(cookies: Map<String, String>): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
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