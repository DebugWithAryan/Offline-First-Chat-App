package com.aryan.offlinefirstchatapp.data.remote.api

import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @POST("messages/batch")
    suspend fun syncMessages(
        @Body messages: List<WebSocketMessage>
    ): Response<SyncResponse>

    @GET("messages/{chatId}")
    suspend fun getMessageHistory(
        @Path("chatId") chatId: String,
        @Query("since") since: Long
    ): Response<List<WebSocketMessage>>
}