package com.danielchew.zwiftviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.danielchew.zwiftviewer.network.RideUiState
import com.danielchew.zwiftviewer.network.ZwiftPowerEventApi
import com.danielchew.zwiftviewer.network.provideAuthenticatedClient
import com.danielchew.zwiftviewer.viewmodel.RideViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ZwiftDebug", "App launched")
        super.onCreate(savedInstanceState)

        setContent {
            val cookiesState = remember { mutableStateOf<Map<String, String>?>(null) }

            if (cookiesState.value == null) {
                ZwiftPowerLoginScreen { extractedCookies ->
                    cookiesState.value = extractedCookies
                }
            } else {
                val client = provideAuthenticatedClient(cookiesState.value!!)
                val api = ZwiftPowerEventApi(client)
                val viewModel = remember { RideViewModel(api) }

                LaunchedEffect(Unit) {
                    viewModel.loadRides()
                }
                val rideState by viewModel.state.collectAsState()

                when (rideState) {
                    is RideUiState.Loading -> Text("Loading rides...")
                    is RideUiState.Error -> Text("Error: ${(rideState as RideUiState.Error).message}")
                    is RideUiState.Success -> {
                        val rides = (rideState as RideUiState.Success).rides
                        LazyColumn {
                            items(rides.size) { index ->
                                Text(rides[index].name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LazyColumn {
        items(2) {
            Text("Sample Ride #$it")
        }
    }
}