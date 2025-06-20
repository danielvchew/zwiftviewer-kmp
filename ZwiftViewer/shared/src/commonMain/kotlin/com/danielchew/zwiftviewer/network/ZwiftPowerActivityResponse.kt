package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.viewmodel.StatWrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("ZwiftPowerActivityResponse")
@Serializable
data class ZwiftPowerActivityResponse(
    @SerialName("data")
    val data: List<DataItem>
) {
    @ObjCName("ZwiftPowerActivityDataItem")
    @Serializable
    data class DataItem(
        @SerialName("zaid") val id: String? = null,
        @SerialName("title") val name: String? = null,
        @SerialName("date") val date: Long? = null,
        @SerialName("avg_cadence") val avgCadence: StatWrapper? = null,
        @SerialName("avg_hr") val avgHr: StatWrapper? = null,
        @SerialName("avg_power") val avgPower: StatWrapper? = null,
        @SerialName("distance") val distance: Double? = null,
        @SerialName("elevation") val elevation: Int? = null,
        @SerialName("avg_speed") val avgSpeed: Double? = null,
        @SerialName("calories") val calories: Int? = null,
        @SerialName("elapsed") val elapsed: StatWrapper? = null,
        @SerialName("max_hr") val maxHr: StatWrapper? = null,
        @SerialName("sport") val sport: String? = null
    )
}
