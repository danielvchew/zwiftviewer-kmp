package com.danielchew.zwiftviewer

import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.Composable
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
                            println("ZwiftDebug: Page finished loading: $url")
                            run {
                                val cookieManager = CookieManager.getInstance()
                                val rawCookies = cookieManager.getCookie(url)
                                if (!rawCookies.isNullOrBlank()) {
                                    println("ZwiftDebug: [Fallback] Attempting cookie extraction even without profile.php")
                                    val parsedCookies = rawCookies
                                        .split(";")
                                        .mapNotNull { it.trim().split("=").takeIf { it.size == 2 } }
                                        .associate { it[0] to it[1] }
                                    println("ZwiftDebug: [Fallback] Parsed cookie keys: ${parsedCookies.keys}")
                                    onCookiesExtracted(parsedCookies)
                                    println("ZwiftDebug: ✅ [Fallback] onCookiesExtracted invoked.")
                                }
                            }
                            if (url?.contains("profile.php") == true) {
                                println("ZwiftDebug: Detected profile.php — likely login success.")
                                val cookieManager = CookieManager.getInstance()
                                val rawCookies = cookieManager.getCookie(url)
                                if (rawCookies.isNullOrBlank()) {
                                    println("ZwiftDebug: ❌ No cookies returned for $url. Aborting.")
                                    return
                                }
                                println("ZwiftDebug: Raw cookie string: $rawCookies")
                                val parsedCookies = rawCookies
                                    .split(";")
                                    .mapNotNull { it.trim().split("=").takeIf { it.size == 2 } }
                                    .associate { it[0] to it[1] }
                                println("ZwiftDebug: Parsed cookie keys: ${parsedCookies.keys}")
                                println("ZwiftDebug: ✅ Extracted cookies passed to Main: ${parsedCookies.keys}")
                                onCookiesExtracted(parsedCookies)
                                onLoginSuccess()
                                println("ZwiftDebug: ✅ onCookiesExtracted invoked.")
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
