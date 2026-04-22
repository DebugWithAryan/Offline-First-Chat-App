package com.aryan.offlinefirstchatapp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketClient
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import com.aryan.offlinefirstchatapp.domain.usecase.GetMessagesUseCase
import com.aryan.offlinefirstchatapp.domain.usecase.SaveIncomingMessageUseCase
import com.aryan.offlinefirstchatapp.domain.usecase.SendMessageUseCase
import com.aryan.offlinefirstchatapp.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val saveIncomingMessageUseCase: SaveIncomingMessageUseCase,
    private val webSocketClient: WebSocketClient,
) : ViewModel(){

    private val _messages = MutableStateFlow<UiState<List<Message>>>(UiState.Loading)
    val messages: StateFlow<UiState<List<Message>>> = _messages.asStateFlow()

    private val _currentChatId = MutableStateFlow<String?>(null)
    private val _currentUserId = MutableStateFlow<String?>(null)

    fun loadChat(chatId: String, userId: String){
        _currentChatId.value = chatId
        _currentUserId.value = userId
        observeMessages(chatId)
        connectWebSocket(userId)
    }

    private fun observeMessages(chatId: String){
        viewModelScope.launch {
            getMessagesUseCase(chatId)
                .catch { e ->
                    _messages.value = UiState.Error(
                        e.message?: "Unknown error"
                    )
                }
                .collect{ messages ->
                    _messages.value = UiState.Success(messages)
                }
        }
    }

    private fun connectWebSocket(userId: String){
        viewModelScope.launch {
            webSocketClient.connect(
                userId = userId,
                onMessageReceived = { wsMessage ->
                    viewModelScope.launch {
                        saveIncomingMessageUseCase(
                            Message(
                                id = wsMessage.id,
                                chatId = wsMessage.chatId,
                                senderId = wsMessage.senderId,
                                content = wsMessage.content,
                                syncStatus = SyncStatus.SENT,
                                timestamp = wsMessage.timestamp
                            )
                        )
                    }
                },
                onConnectionLost = {

                }
            )
        }
    }

    fun sendMessage(content: String){
        viewModelScope.launch {
            val message = Message(
                id = UUID.randomUUID().toString(),
                chatId = _currentChatId.value ?: return@launch,
                senderId = _currentUserId.value ?: return@launch,
                content = content,
                timeStamp = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
            sendMessageUseCase(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }

    fun retryMessage(messageId: String){
        viewModelScope.launch {

        }
    }

}