package com.danielchew.zwiftviewer.utils

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
}
