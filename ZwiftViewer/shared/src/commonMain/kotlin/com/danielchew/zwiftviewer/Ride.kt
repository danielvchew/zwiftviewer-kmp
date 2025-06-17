@file:OptIn(ExperimentalObjCName::class)

package com.danielchew.zwiftviewer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@Serializable
@ObjCName(("ZRide"))
data class Ride (
    val id: String,
    val name: String,
    @SerialName("distanceInMeters")
    val distance: Double,
    val duration: Int
)