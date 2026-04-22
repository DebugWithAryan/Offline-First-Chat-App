package com.aryan.offlinefirstchatapp.data.mapper

import com.aryan.offlinefirstchatapp.data.local.entity.MessageEntity
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus

object MessageMapper {

    fun MessageEntity.toDomain(): Message{
        return Message(
            id = id,
            chatId = chatId,
            senderId = senderId,
            content = content,
            timestamp = timestamp,
            syncStatus = SyncStatus.valueOf(syncStatus)

        )
    }

    fun Message.toEntity(): MessageEntity{
        return MessageEntity(
            id = id,
            chatId = chatId,
            senderId = senderId,
            content = content,
            timestamp = timestamp,
            syncStatus = syncStatus.name
        )
    }
}