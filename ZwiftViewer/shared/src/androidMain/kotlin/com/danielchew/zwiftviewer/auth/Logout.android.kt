package com.danielchew.zwiftviewer.auth

import android.webkit.CookieManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.header

private val client = HttpClient(OkHttp)

actual suspend fun logout(cookieHeader: String) {
    client.get("https://zwiftpower.com/ucp.php?mode=logout") {
        header("Cookie", cookieHeader)
    }
}

actual fun clearAllZwiftCookies() {
    CookieManager.getInstance().removeAllCookies(null)
    CookieManager.getInstance().flush()
}