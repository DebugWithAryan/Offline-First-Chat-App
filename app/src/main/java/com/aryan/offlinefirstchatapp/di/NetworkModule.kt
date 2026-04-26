package com.aryan.offlinefirstchatapp.di

import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.data.remote.api.AuthApiService
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

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService{
        return retrofit.create(AuthApiService::class.java)
    }
}