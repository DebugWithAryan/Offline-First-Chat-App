package com.aryan.offlinefirstchatapp.domain.repository

import com.aryan.offlinefirstchatapp.domain.model.Message

interface ChatConnectionRepository {
    fun disconnect()
    fun connect(
        userId: String,
        onMessageReceived: (Message) -> Unit,
        onConnectionLost: () -> Unit

    )
}