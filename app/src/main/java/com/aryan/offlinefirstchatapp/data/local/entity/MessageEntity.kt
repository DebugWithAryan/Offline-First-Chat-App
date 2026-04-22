package com.aryan.offlinefirstchatapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val sequence: Long = 0L,
    val syncStatus: String
)
