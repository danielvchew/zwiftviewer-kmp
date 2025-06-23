package com.danielchew.zwiftviewer.ui.login

import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ZwiftPowerLoginScreen(
    onCookiesExtracted: (Map<String, String>) -> Unit,
    onPageUrlChanged: (String) -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier,
    urlToLoad: String
) {
    Box(modifier = modifier) {
        AndroidView(
            factory = { context: Context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            if (url == null) {
                                println("ZwiftDebug: onPageFinished called with null URL.")
                                return
                            }

                            println("ZwiftDebug: Page finished loading: $url")

                            val cookieManager = CookieManager.getInstance()
                            val rawCookies = cookieManager.getCookie(url)
                            if (rawCookies.isNullOrBlank()) {
                                println("ZwiftDebug: No cookies found for $url")
                                return
                            }

                            val parsedCookies = rawCookies
                                .split(";")
                                .mapNotNull { it.trim().split("=").takeIf { it.size == 2 } }
                                .associate { it[0] to it[1] }

                            println("ZwiftDebug: Parsed cookie keys: ${parsedCookies.keys}")
                            onCookiesExtracted(parsedCookies)

                            if (url.contains("profile.php")) {
                                println("ZwiftDebug: Detected profile.php – assuming login success.")
                                onLoginSuccess()
                            }
                        }
                    }
                    loadUrl(urlToLoad)
                }
            },
            modifier = modifier
        )
    }
}
