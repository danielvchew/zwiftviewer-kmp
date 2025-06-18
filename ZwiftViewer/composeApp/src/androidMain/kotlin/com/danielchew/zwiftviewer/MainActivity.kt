package com.danielchew.zwiftviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.danielchew.zwiftviewer.network.KtorClientProvider.provideAuthenticatedClient
import com.danielchew.zwiftviewer.network.RideUiState
import com.danielchew.zwiftviewer.network.ZwiftPowerProfileApi
import com.danielchew.zwiftviewer.ui.RideCard
import com.danielchew.zwiftviewer.viewmodel.RideViewModel
import com.danielchew.zwiftviewer.util.extractCookiesForDomain
import com.danielchew.zwiftviewer.utils.extractZwiftPowerUserId


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ZwiftDebug", "App launched")
        super.onCreate(savedInstanceState)

        setContent {
            Log.d("ZwiftDebug", "Log is working")
            val cookiesState = remember { mutableStateOf<Map<String, String>?>(null) }
            val isLoggedInState = remember { mutableStateOf<Boolean?>(null) }

            // Move extraction inside composition
            LaunchedEffect(Unit) {
                val parsedCookies = extractCookiesForDomain("https://zwiftpower.com")
                cookiesState.value = parsedCookies
                Log.d("ZwiftDebug", "Extracted cookies: ${parsedCookies?.keys}")
                Log.d("ZwiftDebug", "All cookies: ${cookiesState.value?.entries}")
            }

            when (val parsedCookies = cookiesState.value) {
                null -> {
                    // Still loading cookies — show a loading state or nothing
                    Text("Loading...")
                }

                else -> {
                    if (parsedCookies.isEmpty()) {
                        ZwiftPowerLoginScreen { extractedCookies: Map<String, String> ->
                            cookiesState.value = extractedCookies
                        }
                        return@setContent
                    } else {
                        // Prepare client, api, and viewModel before LaunchedEffect
                        val client = provideAuthenticatedClient(parsedCookies)
                        val api = ZwiftPowerProfileApi(client)
                        val viewModel = remember(parsedCookies) { RideViewModel(api) }

                        // Check login state in LaunchedEffect, set isLoggedInState, and load rides if possible
                        LaunchedEffect(parsedCookies) {
                            val profileId = extractZwiftPowerUserId(client)
                            val isLoggedIn = profileId != null
                            if (profileId == null) {
                                Log.e("ZwiftDebug", "Failed to extract ZwiftPower user ID. Login likely failed.")
                            } else {
                                Log.d("ZwiftDebug", "Parsed ZwiftPower user ID: $profileId")
                            }

                            if (isLoggedIn && profileId != null) {
                                viewModel.loadRides(profileId, parsedCookies)
                            } else {
                                Log.d("ZwiftDebug", "Login failed or profileId is null.")
                            }

                            isLoggedInState.value = isLoggedIn
                        }

                        when (isLoggedInState.value) {
                            null -> {
                                Text("Checking login...")
                            }
                            false -> {
                                ZwiftPowerLoginScreen { newCookies ->
                                    cookiesState.value = newCookies
                                    isLoggedInState.value = null // retry login check after new cookies
                                }
                            }
                            else -> {
                                val rideState by viewModel.state.collectAsState()
                                when (rideState) {
                                    is RideUiState.Loading -> Text("Loading rides...")
                                    is RideUiState.Error -> Text("Error: ${(rideState as RideUiState.Error).message}")
                                    is RideUiState.Success -> {
                                        val rides = (rideState as RideUiState.Success).rides
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(16.dp)
                                        ) {
                                            items(rides) { ride ->
                                                RideCard(ride)
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
