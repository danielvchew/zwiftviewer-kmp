package com.danielchew.zwiftviewer

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebChromeClient
import androidx.compose.runtime.Composable

suspend fun getZwiftPowerRides(response: String): List<ZwiftPowerRide> {
    val rides = Json.decodeFromString<List<ZwiftPowerRide>>(response)
    println("ZwiftDebug: Parsed ${rides.size} rides from response")
    return rides
}

@Composable
fun ZwiftPowerLoginScreen(onCookiesExtracted: (Map<String, String>) -> Unit) {
    var loginComplete by remember { mutableStateOf(false) }

    if (!loginComplete) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context: Context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                if (url?.contains("profile.php") == true) {
                                    val cookieManager = CookieManager.getInstance()
                                    val cookies = cookieManager.getCookie(url)
                                    val parsedCookies = cookies
                                        ?.split(";")
                                        ?.mapNotNull { it.trim().split("=").takeIf { it.size == 2 } }
                                        ?.associate { it[0] to it[1] }
                                        ?: emptyMap()
                                    onCookiesExtracted(parsedCookies)
                                    loginComplete = true
                                }
                            }
                        }
                        loadUrl("https://zwiftpower.com/")
                    }
                }
            )
        }
    }
}
