package com.danielchew.zwiftviewer.viewmodel

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class StatWrapper(
    val list: List<JsonElement>
) {
    fun toInt(): Int? = list.getOrNull(0)?.jsonPrimitive?.intOrNull
    fun label(): String? = list.getOrNull(1)?.jsonPrimitive?.contentOrNull
}