package com.danielchew.zwiftviewer.network

import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect fun getPlatformEngine(): HttpClientEngineFactory<*>

val ktorClient = HttpClient(getPlatformEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

object KtorClientProvider {
    fun provideAuthenticatedClient(cookies: Map<String, String>): HttpClient {
        return HttpClient(getPlatformEngine()) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                if (cookies.isNotEmpty()) {
                    header("Cookie", cookies.entries.joinToString("; ") { "${it.key}=${it.value}" })
                }
            }
        }
    }
}