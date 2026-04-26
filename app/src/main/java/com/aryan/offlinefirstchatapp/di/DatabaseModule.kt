package com.aryan.offlinefirstchatapp.di

import android.content.Context
import androidx.room.Room
import com.aryan.offlinefirstchatapp.data.local.dao.MessageDao
import com.aryan.offlinefirstchatapp.data.local.db.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_database"
        )
            .addMigrations(ChatDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideMessageDao(database: ChatDatabase): MessageDao{
        return database.messageDao()
    }
}