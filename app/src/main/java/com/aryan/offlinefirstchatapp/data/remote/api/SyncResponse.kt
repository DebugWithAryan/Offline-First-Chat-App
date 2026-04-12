package com.aryan.offlinefirstchatapp.data.remote.api

data class SyncResponse(
    val syncedIds: List<String>,
    val failedIds: List<String>
)
