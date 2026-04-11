package com.aryan.offlinefirstchatapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aryan.offlinefirstchatapp.data.local.dao.MessageDao
import com.aryan.offlinefirstchatapp.data.local.entity.MessageEntity


@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}