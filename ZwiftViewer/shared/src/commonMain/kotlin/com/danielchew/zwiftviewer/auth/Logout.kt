package com.danielchew.zwiftviewer.auth

expect suspend fun logout(cookieHeader: String)
expect fun clearAllZwiftCookies()