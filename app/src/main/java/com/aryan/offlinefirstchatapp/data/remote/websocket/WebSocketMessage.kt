package com.aryan.offlinefirstchatapp.data.remote.websocket


data class WebSocketMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)
