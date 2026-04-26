package com.aryan.offlinefirstchatapp.data.remote.websocket

import android.R.id.message
import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import com.aryan.offlinefirstchatapp.domain.repository.ChatConnectionRepository
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebSocketClient(private val sessionManager: SessionManager): ChatConnectionRepository {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    override fun connect(
        userId: String,
        onMessageReceived: (Message) -> Unit,
        onConnectionLost: () -> Unit
    ) {
        val token = sessionManager.getToken()
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/chat?$userId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val wsMessage = gson.fromJson(text, WebSocketMessage::class.java)

                val message = Message(
                    id = wsMessage.id,
                    chatId = wsMessage.chatId,
                    senderId = wsMessage.senderId,
                    content = wsMessage.content,
                    timestamp = wsMessage.timestamp,
                    syncStatus = SyncStatus.SENT
                )
                onMessageReceived(message)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                onConnectionLost()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@WebSocketClient.webSocket = null
            }
        })
    }

    fun sendMessage(message: WebSocketMessage) {
        webSocket?.send(gson.toJson(message))
    }

    override fun disconnect() {
        webSocket?.close(1000, "User Disconnected")
        webSocket = null
    }
}