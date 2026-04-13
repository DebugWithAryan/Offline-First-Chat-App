package com.aryan.offlinefirstchatapp.di

import com.aryan.offlinefirstchatapp.data.remote.api.ChatApiService
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://yourserver.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService{
        return retrofit.create(ChatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient{
        return WebSocketClient(token = "user_token_here")
    }
}