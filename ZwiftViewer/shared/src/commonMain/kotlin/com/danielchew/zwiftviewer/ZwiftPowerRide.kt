package com.danielchew.zwiftviewer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZwiftPowerRide(
    val date: Long,
    val zaid: String,
    val title: String,
    val zid: String,
    val elapsed: List<Int>,
    val distance: Int,
    @SerialName("world_id") val worldId: String,
    val sport: String,
    val fit: String,
    val aid: String,
    @SerialName("avg_speed") val avgSpeed: Int,
    @SerialName("avg_hr") val avgHr: List<Int>,
    @SerialName("max_hr") val maxHr: List<Int>,
    @SerialName("avg_cadence") val avgCadence: List<Int>,
    val calories: Int,
    @SerialName("avg_power") val avgPower: List<Int>,
    val elevation: Int,
    val zeid: Int
)