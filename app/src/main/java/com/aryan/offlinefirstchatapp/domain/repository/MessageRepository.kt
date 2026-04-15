package com.aryan.offlinefirstchatapp.domain.repository

import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage (message: Message)
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun getPendingMessages(): List<Message>
    suspend fun  updateSyncStatus(messageId: String, status: SyncStatus)
    suspend fun retryFailedMessage(messageId: String)
}