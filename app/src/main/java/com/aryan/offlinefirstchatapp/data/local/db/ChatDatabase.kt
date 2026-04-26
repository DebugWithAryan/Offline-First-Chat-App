package com.aryan.offlinefirstchatapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aryan.offlinefirstchatapp.data.local.dao.MessageDao
import com.aryan.offlinefirstchatapp.data.local.entity.MessageEntity


@Database(
    entities = [MessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE messages ADD COLUMN sequence INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}