package com.aryan.offlinefirstchatapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aryan.offlinefirstchatapp.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY sequence ASC, timestamp ASC")
    fun getMessages(chatId: String): Flow<List<MessageEntity>>
    @Query("SELECT * FROM messages WHERE syncStatus = 'PENDING'")
    suspend fun getPendingMessages(): List<MessageEntity>

    @Query("UPDATE messages SET syncStatus = :status WHERE id = :messageId")
    suspend fun updateSyncStatus(messageId: String, status: String)
}