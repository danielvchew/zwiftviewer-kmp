package com.danielchew.zwiftviewer.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Rides : Screen("rides")
    data class RideDetail(val rideId: String) : Screen("rideDetail/$rideId") {
        companion object {
            const val route = "rideDetail/{rideId}"
        }
    }
}
