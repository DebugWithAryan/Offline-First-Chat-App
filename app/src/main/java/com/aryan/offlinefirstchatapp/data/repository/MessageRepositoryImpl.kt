package com.aryan.offlinefirstchatapp.data.repository

import com.aryan.offlinefirstchatapp.data.local.dao.MessageDao
import com.aryan.offlinefirstchatapp.data.mapper.MessageMapper.toDomain
import com.aryan.offlinefirstchatapp.data.mapper.MessageMapper.toEntity
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository
import com.aryan.offlinefirstchatapp.sync.SyncScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val dao: MessageDao,
    private val syncScheduler: SyncScheduler
): MessageRepository {
    override suspend fun sendMessage(message: Message) {
        dao.insertMessage(message.toEntity())
        syncScheduler.schedule()
    }

    override fun getMessages(chatId: String): Flow<List<Message>> {
        return  dao.getMessages(chatId).map{ entities ->
            entities.map { it.toDomain() }

        }
    }

    override suspend fun getPendingMessages(): List<Message> {
        return dao.getPendingMessages().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(messageId: String, status: SyncStatus) {
        dao.updateSyncStatus(messageId, status.name)
    }

    override suspend fun retryFailedMessage(messageId: String) {
        dao.updateSyncStatus(messageId, SyncStatus.PENDING.name)
        syncScheduler.schedule()
    }
}