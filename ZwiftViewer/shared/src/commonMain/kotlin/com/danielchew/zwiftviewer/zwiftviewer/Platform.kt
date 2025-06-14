package com.danielchew.zwiftviewer.zwiftviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform