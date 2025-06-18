package com.danielchew.zwiftviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielchew.zwiftviewer.ui.login.LoginScreen
import com.danielchew.zwiftviewer.ui.ridelist.RideListScreen

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

    Surface(color = MaterialTheme.colorScheme.background) {
        AppNavigation(navController = navController)
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("rides") {
            RideListScreen(navController)
        }
        composable("rideDetail/{rideId}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId") ?: ""
            com.danielchew.zwiftviewer.ui.ridedetail.RideDetailScreen(rideId)
        }
    }
}
