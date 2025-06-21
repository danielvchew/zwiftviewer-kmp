package com.danielchew.zwiftviewer.utils

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText

object AndroidCookieStore : CookieStore {
    private var storedCookies: Map<String, String> = emptyMap()

    override fun save(cookies: Map<String, String>) {
        storedCookies = cookies
    }

    override fun get(): Map<String, String> {
        return storedCookies
    }

    override fun setCookies(cookies: Map<String, String>, domain: String) {
        val manager = android.webkit.CookieManager.getInstance()
        for ((key, value) in cookies) {
            manager.setCookie("https://$domain", "$key=$value")
        }
        manager.flush() // Ensure cookies are written immediately
    }

    override fun getCookies(domain: String): Map<String, String> {
        val manager = android.webkit.CookieManager.getInstance()
        val rawCookieHeader = manager.getCookie("https://$domain") ?: return emptyMap()

        return rawCookieHeader.split(";")
            .mapNotNull {
                val parts = it.trim().split("=")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
    }

    override fun clearCookies(domain: String) {
        val manager = android.webkit.CookieManager.getInstance()
        val cookies = getCookies(domain)
        for (key in cookies.keys) {
            manager.setCookie("https://$domain", "$key=; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
        }
        manager.flush()
    }

    override fun getZwiftId(): String? {
        val cookies = getCookies("zwiftpower.com")
        android.util.Log.d("ZwiftDebug", "getZwiftId: available cookies = $cookies")

        val zwiftId = cookies["phpbb3_lswlk_u"]
        if (zwiftId == null) {
            android.util.Log.d("ZwiftDebug", "getZwiftId: Zwift ID not found in cookies")
        } else {
            android.util.Log.d("ZwiftDebug", "getZwiftId: Found Zwift ID = $zwiftId")
        }
        android.util.Log.d("ZwiftDebug", "getZwiftId: Returning zwiftId = $zwiftId")
        return zwiftId
    }

    override suspend fun legacyGetZwiftId(): String? {
        android.util.Log.d("ZwiftDebug", "legacyGetZwiftId: Starting legacy Zwift ID extraction")
        val client = io.ktor.client.HttpClient {
            install(io.ktor.client.plugins.cookies.HttpCookies)
        }

        return try {
            val cookieHeader = getCookies("zwiftpower.com")
                .entries.joinToString("; ") { (k, v) -> "$k=$v" }

            val response = client.get("https://zwiftpower.com/profile.php") {
                header(io.ktor.http.HttpHeaders.Cookie, cookieHeader)
            }
            val body = response.bodyAsText()
            android.util.Log.d("ZwiftDebug", "legacyGetZwiftId: Fetched profile.php")

            val regex = """profile\.php\?z=(\d+)""".toRegex()
            val match = regex.find(body)
            val zwiftId = match?.groupValues?.getOrNull(1)

            if (zwiftId != null) {
                android.util.Log.d("ZwiftDebug", "legacyGetZwiftId: Extracted Zwift ID = $zwiftId")
            } else {
                android.util.Log.d("ZwiftDebug", "legacyGetZwiftId: Zwift ID not found in profile.php")
            }

            android.util.Log.d("ZwiftDebug", "legacyGetZwiftId: Returning zwiftId = $zwiftId")
            zwiftId
        } catch (e: Exception) {
            android.util.Log.e("ZwiftDebug", "legacyGetZwiftId: Exception - ${e.message}")
            null
        } finally {
            client.close()
        }
    }
}
