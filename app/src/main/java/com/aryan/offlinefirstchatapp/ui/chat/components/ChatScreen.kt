package com.aryan.offlinefirstchatapp.ui.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aryan.offlinefirstchatapp.ui.chat.ChatViewModel
import com.aryan.offlinefirstchatapp.ui.common.UiState

@Composable
fun ChatScreen (
    chatId: String,
    currentUserId: String,
    viewModel: ChatViewModel = hiltViewModel()
){
    val messageState by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
    }

    val messages = when (val state = messageState){
        is UiState.Success -> state.data
        else -> emptyList()
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()){
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        bottomBar = {
            MessageInput(
                onSendMessage = {content ->
                    viewModel.sendMessage(content, currentUserId)

                }
            )
        }
    ) { paddingValues ->

        when (val state = messageState){
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = state.data,
                        key = { message -> message.id}
                    ){message ->
                        MessageItem(
                            message = message,
                            currentUserId = currentUserId,
                            onRetry = {messageId ->
                                viewModel.retryMessage(messageId)

                            }
                        )
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

    }
}