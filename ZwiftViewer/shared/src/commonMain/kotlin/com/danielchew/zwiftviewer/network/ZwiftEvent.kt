package com.danielchew.zwiftviewer.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZwiftEvent (
    @SerialName("event_id") val id: String,
    @SerialName("event_name") val name: String,
    @SerialName("event_time") val startTime: String,
    @SerialName("event_type") val type: String,
            val route: RouteInfo? = null
)

@Serializable
data class RouteInfo(
    val world: String,
    val route: String
)