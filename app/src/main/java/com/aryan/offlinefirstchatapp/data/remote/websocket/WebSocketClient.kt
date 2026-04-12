package com.aryan.offlinefirstchatapp.data.remote.websocket

import com.aryan.offlinefirstchatapp.domain.model.Message
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient(
    private val token: String
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    fun connect(onMessageReceived: (WebSocketMessage) -> Unit){
        val request = Request.Builder()
            .url("ws://yourserver.com/chat")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener(){
            override fun onMessage(webSocket: WebSocket, text: String) {
                val message = gson.fromJson(text, WebSocketMessage::class.java)
                onMessageReceived(message)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // SyncWorker will handle retry via WorkManager
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@WebSocketClient.webSocket = null
            }

        })
    }

    fun sendMessage(message: Message){
        val wsMessage = WebSocketMessage(
            id = message.id,
            chatId = message.chatId,
            senderId = message.senderId,
            content = message.content,
            timestamp = message.timeStamp
        )
        webSocket?.send(gson.toJson(wsMessage))
    }

    fun disconnect(){
        webSocket?.close(1000, "User Disconnected")
        webSocket = null
    }
}