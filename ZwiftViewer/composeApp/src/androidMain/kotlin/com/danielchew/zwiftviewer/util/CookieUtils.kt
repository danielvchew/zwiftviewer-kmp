package com.danielchew.zwiftviewer.util

import android.util.Log
import android.webkit.CookieManager

fun extractCookiesForDomain(domain: String): Map<String, String> {
    val cookieManager = CookieManager.getInstance()
    val rawCookies = cookieManager.getCookie(domain) ?: return emptyMap()

    val parsedCookies = rawCookies.split(";").mapNotNull {
        val trimmed = it.trim()
        val parts = trimmed.split("=", limit = 2)
        if (parts.size == 2 && parts[0].isNotBlank()) {
            parts[0] to parts[1]
        } else {
            null // skip malformed cookies
        }
    }.toMap()

    Log.d("ZwiftDebug", "Parsed cookies for $domain: ${parsedCookies.keys}")
    return parsedCookies
}