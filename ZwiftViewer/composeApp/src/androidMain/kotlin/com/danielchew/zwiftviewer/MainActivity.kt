package com.danielchew.zwiftviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielchew.zwiftviewer.bridge.RideApi
import com.danielchew.zwiftviewer.ui.login.LoginScreen
import com.danielchew.zwiftviewer.ui.ridedetail.RideDetailScreen
import com.danielchew.zwiftviewer.ui.ridelist.RideListScreen
import com.danielchew.zwiftviewer.utils.CookieStoreInstance
import com.danielchew.zwiftviewer.viewmodel.RideViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val rideViewModel = remember { RideViewModel(api = RideApi()) }

    ZwiftPowerLoginScreen(
        modifier = Modifier.fillMaxSize(),
        urlToLoad = "https://zwiftpower.com/",
        onCookiesExtracted = { cookies ->
            println("ZwiftDebug: 📦 Cookies received in MainActivity: ${cookies.keys}")
            println("ZwiftDebug: 📦 Full cookie map: $cookies")
            println("ZwiftDebug: ✅ Cookies received: ${cookies.keys}")
            println("ZwiftDebug: Full cookies map: $cookies")
            if (!cookies.containsKey("CloudFront-Policy") ||
                !cookies.containsKey("CloudFront-Key-Pair-Id") ||
                !cookies.containsKey("CloudFront-Signature")) {
                println("ZwiftDebug: ❌ Missing CloudFront cookies — likely not fully authenticated.")
            }

            if (cookies.isNotEmpty() && cookies.keys.any { it.contains("CloudFront") }) {
                println("ZwiftDebug: 🟡 Cookie check passed — proceeding to API call")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        CookieStoreInstance.setCookies(domain = "zwiftpower.com", cookies = cookies)
                        val rides = RideApi().getUserRideHistory("2109820", cookies)
                        println("ZwiftDebug: ✅ API returned ${rides.size} rides")
                        rides.forEachIndexed { index, ride ->
                            println("ZwiftDebug: Ride $index: ${ride.name} on ${ride.date}")
                        }
                        rideViewModel.setRides(rides)
                    } catch (e: Exception) {
                        println("ZwiftDebug: ❌ API error: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } else {
                println("ZwiftDebug: 🔴 No valid cookies — skipping API call")
            }
        },
        onPageUrlChanged = { url ->
            println("ZwiftDebug: Page changed: $url")
        }
    )
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "rides") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("rides") {
            RideListScreen(navController)
        }
        composable("rideDetail/{rideId}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: ""
            RideDetailScreen(rideId)
        }
    }
}