package com.aryan.offlinefirstchatapp.di

import android.content.Context
import com.aryan.offlinefirstchatapp.data.local.dao.MessageDao
import com.aryan.offlinefirstchatapp.data.repository.MessageRepositoryImpl
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository
import com.aryan.offlinefirstchatapp.sync.SyncScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSyncScheduler(
        @ApplicationContext context: Context
    ): SyncScheduler{
        return SyncScheduler(context)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        dao: MessageDao,
        syncScheduler: SyncScheduler
    ): MessageRepository{
        return MessageRepositoryImpl(dao, syncScheduler)
    }
}