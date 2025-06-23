package com.danielchew.zwiftviewer.network

import com.danielchew.zwiftviewer.viewmodel.StatWrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("ZwiftPowerActivityResponse")
@Serializable
data class ActivityResponse(
    @SerialName("data")
    val data: List<Ride>
)

