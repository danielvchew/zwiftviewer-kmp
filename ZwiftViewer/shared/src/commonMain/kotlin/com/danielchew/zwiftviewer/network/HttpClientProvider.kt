package com.danielchew.zwiftviewer.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            // safely ignore extra fields in JSON
            ignoreUnknownKeys = true
        })
    }

    fun provideAuthenticatedClient(cookies: Map<String, String>): HttpClient {
        return HttpClient(CIO) {
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