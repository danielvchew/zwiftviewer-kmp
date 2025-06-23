package com.danielchew.zwiftviewer.navigation

import android.util.Log
import com.danielchew.zwiftviewer.utils.CookieStoreInstance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.danielchew.zwiftviewer.ui.login.ZwiftPowerLoginScreen
import com.danielchew.zwiftviewer.ui.ridelist.RideListScreen
import com.danielchew.zwiftviewer.ui.ridedetail.RideDetailScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    urlToLoad: String
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            Log.d("ZwiftDebug", "Navigated to LoginScreen")
            ZwiftPowerLoginScreen(
                onCookiesExtracted = {
                    CookieStoreInstance.setCookies(it, domain = "zwiftpower.com")
                },
                onPageUrlChanged = {},
                onLoginSuccess = {
                    Log.d("ZwiftDebug", "Login successful. Navigating to RideListScreen")
                    navController.navigate(Screen.Rides.route)
                },
                modifier = modifier,
                urlToLoad = urlToLoad
            )
        }

        composable(Screen.Rides.route) {
            Log.d("ZwiftDebug", "Navigated to RideListScreen")
            RideListScreen(navController = navController)
        }

        composable(
            route = Screen.RideDetail.route,
            arguments = listOf(navArgument("rideId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: return@composable
            Log.d("ZwiftDebug", "Navigated to RideDetailScreen with rideId=$rideId")
            RideDetailScreen(rideId = rideId)
        }
    }
}
