package com.danielchew.zwiftviewer

import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ZwiftPowerLoginScreen(onCookiesExtracted: (Map<String, String>) -> Unit) {
    val loginComplete = remember { mutableStateOf(false) }

    if (!loginComplete.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(), // FULL SCREEN
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView, url: String?) {
                                if (url?.contains("profile.php") == true) {
                                    val cookieManager = CookieManager.getInstance()
                                    val cookies = cookieManager.getCookie("https://zwiftpower.com")
                                    val parsedCookies = cookies.split(";").associate {
                                        val parts = it.trim().split("=")
                                        parts[0] to (parts.getOrNull(1) ?: "")
                                    }
                                    onCookiesExtracted(parsedCookies)
                                }
                            }
                        }
                        loadUrl("https://zwiftpower.com")
                    }
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Login complete. Rides will load soon.")
        }
    }
}
