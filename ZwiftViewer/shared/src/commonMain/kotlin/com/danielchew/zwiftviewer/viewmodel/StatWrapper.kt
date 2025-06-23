@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.danielchew.zwiftviewer.viewmodel

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

@Serializable(with = StatWrapper.Serializer::class)
sealed class StatWrapper {
    abstract fun toInt(): Int?
    abstract fun label(): String?

    val rawValue: JsonElement?
        get() = when (this) {
            is SingleWrapper -> value
            is ListWrapper -> list.getOrNull(0)
        }

    fun toEpochSecondsOrNull(): Long? = when (this) {
        is StatWrapper.SingleWrapper -> value.jsonPrimitive.longOrNull
        is StatWrapper.ListWrapper -> list.getOrNull(0)?.jsonPrimitive?.longOrNull
    }

    val doubleValue: Double?
        get() = when (this) {
            is SingleWrapper -> value.jsonPrimitive.doubleOrNull
            is ListWrapper -> list.getOrNull(0)?.jsonPrimitive?.doubleOrNull
        }

    @Serializable
    data class ListWrapper(val list: List<JsonElement>) : StatWrapper() {
        override fun toInt(): Int? = list.getOrNull(0)?.jsonPrimitive?.intOrNull
        override fun label(): String? = list.getOrNull(0)?.jsonPrimitive?.contentOrNull
    }

    @Serializable
    data class SingleWrapper(val value: JsonElement) : StatWrapper() {
        override fun toInt(): Int? = value.jsonPrimitive.intOrNull
        override fun label(): String? = value.jsonPrimitive.contentOrNull
    }

    object Serializer : KSerializer<StatWrapper> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("StatWrapper")

        override fun deserialize(decoder: Decoder): StatWrapper {
            val input = decoder as? JsonDecoder
                ?: throw SerializationException("Expected JsonDecoder")
            val element = input.decodeJsonElement()

            return when {
                element is JsonArray -> ListWrapper(element)
                else -> SingleWrapper(element)
            }
        }

        override fun serialize(encoder: Encoder, value: StatWrapper) {
            val output = encoder as? JsonEncoder
                ?: throw SerializationException("Expected JsonEncoder")

            when (value) {
                is ListWrapper -> output.encodeJsonElement(JsonArray(value.list))
                is SingleWrapper -> output.encodeJsonElement(value.value)
            }
        }
    }
}

val StatWrapper.doubleValue: Double?
    get() = when (this) {
        is StatWrapper.SingleWrapper -> this.value.jsonPrimitive.doubleOrNull
        is StatWrapper.ListWrapper -> this.list.getOrNull(0)?.jsonPrimitive?.doubleOrNull
    }

val StatWrapper.secondsValue: Double?
    get() = when (this) {
        is StatWrapper.SingleWrapper -> this.value.jsonPrimitive.doubleOrNull
        is StatWrapper.ListWrapper -> this.list.getOrNull(0)?.jsonPrimitive?.doubleOrNull
    }
