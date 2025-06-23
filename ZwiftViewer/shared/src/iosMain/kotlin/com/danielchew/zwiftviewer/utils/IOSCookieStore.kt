package com.danielchew.zwiftviewer.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSHTTPCookie
import platform.Foundation.NSHTTPCookieStorage
import platform.Foundation.NSURL

object IOSCookieStore : CookieStore {
    private val cookieStorage = NSHTTPCookieStorage.sharedHTTPCookieStorage

    private val memoryStorage = mutableMapOf<String, String>()

    override fun save(cookies: Map<String, String>) {
        memoryStorage.putAll(cookies)
        println("ZwiftDebug: IOSCookieStore : Full cookie map saved = $cookies")
        // If profileUrl is included in cookies, store it with a dedicated key
        cookies["profileUrl"]?.let {
            memoryStorage["zwiftIdProfileUrl"] = it
            println("ZwiftDebug: IOSCookieStore : Saved profileUrl = $it")
        }
    }

    override fun get(): Map<String, String> {
        println("ZwiftDebug: IOSCookieStore : memoryStorage contents = $memoryStorage")
        return memoryStorage.toMap()
    }

    override fun getCookies(domain: String): Map<String, String> {
        val url = NSURL(string = domain)
        val cookies = cookieStorage.cookiesForURL(url) ?: return emptyMap()
        return cookies
            .filterIsInstance<NSHTTPCookie>()
            .associate { it.name to it.value }
    }

    override fun setCookies(cookies: Map<String, String>, domain: String) {
        val host = NSURL(string = domain).host ?: ""
        val properties = cookies.map {
            mapOf(
                "Name" to it.key,
                "Value" to it.value,
                "Domain" to host,
                "Path" to "/"
            )
        }

        properties.forEach {
            val safeProps = it.entries.associate { entry -> entry.key as Any? to entry.value as Any? }
            val cookie = NSHTTPCookie.cookieWithProperties(safeProps)
            if (cookie != null) {
                cookieStorage.setCookie(cookie)
            }
        }
    }

    override fun clearCookies(domain: String) {
        val url = NSURL(string = domain)
        val existingCookies = cookieStorage.cookiesForURL(url) ?: return
        existingCookies.forEach {
            if (it is NSHTTPCookie) {
                cookieStorage.deleteCookie(it)
            }
        }
    }

    override fun getZwiftId(): String? {
        return null
    }

    override suspend fun legacyGetZwiftId(): String? = withContext(Dispatchers.IO) {
        val stored = memoryStorage["zwiftIdProfileUrl"]
        if (!stored.isNullOrEmpty()) {
            println("ZwiftDebug: IOSCookieStore : Zwift ID profile URL loaded from memoryStorage: $stored")
            val match = Regex("z=(\\d+)").find(stored)
            val zwiftId = match?.groupValues?.getOrNull(1)
            if (!zwiftId.isNullOrEmpty()) {
                println("ZwiftDebug: IOSCookieStore : Extracted Zwift ID: $zwiftId")
                return@withContext zwiftId
            } else {
                println("ZwiftDebug: IOSCookieStore : Zwift ID not found in stored profile URL")
                return@withContext null
            }
        } else {
            println("ZwiftDebug: IOSCookieStore : Zwift ID profile URL not found in memoryStorage")
            return@withContext null
        }
    }
}