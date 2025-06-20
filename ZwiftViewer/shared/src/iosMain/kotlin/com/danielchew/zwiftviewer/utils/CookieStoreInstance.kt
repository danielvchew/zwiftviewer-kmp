package com.danielchew.zwiftviewer.utils

actual val CookieStoreInstance: CookieStore = object : CookieStore {
    private var storedCookies: Map<String, String> = emptyMap()

    override fun save(cookies: Map<String, String>) {
        storedCookies = cookies
    }

    override fun get(): Map<String, String> {
        return storedCookies
    }
}