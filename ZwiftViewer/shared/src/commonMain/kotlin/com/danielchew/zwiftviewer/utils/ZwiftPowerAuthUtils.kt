package com.danielchew.zwiftviewer.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

fun provideClient(): HttpClient {
    return HttpClient() {
        // Configure logging, user-agent, etc.
    }
}

suspend fun loginToZwiftPower(client: HttpClient, username: String, password: String) {
    client.submitForm(
        url = "https://zwiftpower.com/ucp.php?mode=login",
        formParameters = Parameters.build {
            append("username", username)
            append("password", password)
            append("login", "Login")
        },
        encodeInQuery = false
    )
}

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
