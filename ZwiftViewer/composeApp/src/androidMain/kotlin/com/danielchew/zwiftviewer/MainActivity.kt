package com.danielchew.zwiftviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.danielchew.zwiftviewer.utils.CookieStoreInstance
import androidx.navigation.compose.rememberNavController
import com.danielchew.zwiftviewer.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZwiftViewerApp()
        }
    }
}

@Composable
fun ZwiftViewerApp() {
    val navController = rememberNavController()
    val urlToLoad = "https://zwiftpower.com/profile.php"
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    Log.d("ZwiftDebug", "App launched. Login state: $isLoggedIn")

    if (!isLoggedIn) {
        ZwiftPowerLoginScreen(
            urlToLoad = urlToLoad,
            onCookiesExtracted = { cookies ->
                CookieStoreInstance.setCookies(cookies, domain = "zwiftpower.com")
            },
            onPageUrlChanged = { url ->
                Log.d("ZwiftDebug", "Page changed: $url")
            },
            onLoginSuccess = {
                Log.d("ZwiftDebug", "Login successful. Navigating to app.")
                isLoggedIn = true
            },
            modifier = Modifier
        )
    } else {
        AppNavHost(navController = navController, urlToLoad = urlToLoad)
    }
}