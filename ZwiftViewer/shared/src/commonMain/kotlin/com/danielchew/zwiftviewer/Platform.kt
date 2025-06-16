package com.danielchew.zwiftviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform