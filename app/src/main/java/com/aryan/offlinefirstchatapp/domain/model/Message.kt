package com.aryan.offlinefirstchatapp.domain.model

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val sequence: Long = 0L,
    val syncStatus: SyncStatus
)
