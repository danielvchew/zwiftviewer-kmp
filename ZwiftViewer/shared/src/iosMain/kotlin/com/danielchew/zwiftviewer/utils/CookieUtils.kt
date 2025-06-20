package com.danielchew.zwiftviewer.utils

actual fun extractCookiesForDomain(domain: String): Map<String, String> {
    // iOS doesn't support this yet
    return emptyMap()
}