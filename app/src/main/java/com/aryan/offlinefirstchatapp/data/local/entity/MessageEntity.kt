package com.aryan.offlinefirstchatapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timeStamp: Long,
    val syncStatus: String
)
