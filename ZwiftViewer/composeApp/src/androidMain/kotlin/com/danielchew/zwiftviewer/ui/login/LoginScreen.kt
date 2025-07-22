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
                var loginTriggered = false
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
                                // Run JS in the loaded page to find a real Zwift profile link
                                evaluateJavascript(
                                    "(function() {" +
                                        "const link = document.querySelector('a[href*=\"profile.php?z=\"]');" +
                                        "return link ? link.href : '';" +
                                    "})()"
                                ) { jsResult ->
                                    val profileUrl = jsResult.trim('\"')
                                    if (profileUrl.isNotBlank()) {
                                        println("ZwiftDebug: extracted profile URL = $profileUrl")

                                        // Enrich the cookie map so the ViewModel can find the Zwift ID
                                        val enriched = parsedCookies.toMutableMap()
                                        enriched["profileUrl"] = profileUrl
                                        onCookiesExtracted(enriched)

                                        if (!loginTriggered) {
                                            println("ZwiftDebug: firing onLoginSuccess for first time")
                                            loginTriggered = true
                                            onLoginSuccess()
                                        } else {
                                            println("ZwiftDebug: onLoginSuccess already fired, skipping")
                                        }
                                    } else {
                                        println("ZwiftDebug: still unauthenticated – stay on login screen")
                                    }
                                }
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
