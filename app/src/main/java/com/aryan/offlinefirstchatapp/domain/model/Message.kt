package com.aryan.offlinefirstchatapp.domain.model

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timeStamp: Long,
    val syncStatus: SyncStatus
)
