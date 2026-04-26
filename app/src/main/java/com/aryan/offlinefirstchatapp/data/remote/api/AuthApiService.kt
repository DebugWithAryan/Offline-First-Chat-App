package com.aryan.offlinefirstchatapp.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class RegisterRequest(
    val username: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val userId: String,
    val token: String
)

data class UserResponse(
    val id: String,
    val username: String
)

data class CreateChatRequest(
    val userId1: String,
    val userId2: String
)

data class ChatResponse(val chatId: String)
interface AuthApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("auth/users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @POST("chats")
    suspend fun createChat(
        @Body request: CreateChatRequest
    ): Response<ChatResponse>
}