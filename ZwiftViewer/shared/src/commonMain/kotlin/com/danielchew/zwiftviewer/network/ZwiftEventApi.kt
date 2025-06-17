package com.danielchew.zwiftviewer.network

interface ZwiftEventApi {
    suspend fun getZwiftEvents(): List<ZwiftEvent>
}