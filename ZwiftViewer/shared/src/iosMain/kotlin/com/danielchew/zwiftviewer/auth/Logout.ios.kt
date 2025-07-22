package com.danielchew.zwiftviewer.auth

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.request.get
import io.ktor.client.request.header
import platform.WebKit.WKWebsiteDataStore
import platform.Foundation.NSDate
import platform.Foundation.distantPast

private val client = HttpClient(Darwin)

actual suspend fun logout(cookieHeader: String) {
    // Tell ZwiftPower to invalidate the PHPBB session
    client.get("https://zwiftpower.com/ucp.php?mode=logout") {
        header("Cookie", cookieHeader)
    }
}

actual fun clearAllZwiftCookies() {
    // Wipe cookies for zwiftpower.com and secure.zwift.com
    val store = WKWebsiteDataStore.defaultDataStore()
    store.removeDataOfTypes(
        dataTypes = WKWebsiteDataStore.allWebsiteDataTypes(),
        modifiedSince = NSDate.distantPast,
        completionHandler = {}
    )
}