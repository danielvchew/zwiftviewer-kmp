package com.danielchew.zwiftviewer.utils

interface CookieStore {
    fun save(cookies: Map<String, String>)
    fun get(): Map<String, String>
    fun getCookies(domain: String): Map<String, String>
    fun setCookies(cookies: Map<String, String>, domain: String)
    fun clearCookies(domain: String)
    fun getZwiftId(): String?
    suspend fun legacyGetZwiftId(): String?
}