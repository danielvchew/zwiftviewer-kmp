package com.danielchew.zwiftviewer.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("extractZwiftPowerUserId")
suspend fun extractZwiftPowerUserId(client: HttpClient): String? {
    return try {
        val html: String = client.get("https://zwiftpower.com/profile.php").bodyAsText()
        val regex = Regex("""profile\.php\?z=(\d+)""")
        val match = regex.find(html)
        match?.groupValues?.get(1)
    } catch (e: Exception) {
        println("ZwiftDebug: Failed to extract ZwiftPower user ID: ${e.message}")
        null
    }
}
