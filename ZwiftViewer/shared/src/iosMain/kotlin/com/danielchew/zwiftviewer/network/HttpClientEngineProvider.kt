package com.danielchew.zwiftviewer.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformEngine(): HttpClientEngineFactory<*> {
    println("ZwiftDebug: iOS engine = Darwin")
    return Darwin
}
