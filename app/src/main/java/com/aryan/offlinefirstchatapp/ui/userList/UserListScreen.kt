package com.aryan.offlinefirstchatapp.ui.userList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aryan.offlinefirstchatapp.data.remote.api.UserResponse
import com.aryan.offlinefirstchatapp.ui.common.UiState


@Composable
fun UserListScreen(
    onChatOpen: (chatId: String, userId: String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
){
    val userState by viewModel.users.collectAsState()
    val chatState by viewModel.chatState.collectAsState()
    val currentUserId = viewModel.currentUserId

    LaunchedEffect(chatState) {
        if (chatState is UiState.Success) {
            val chatId = (chatState as UiState.Success<String>).data
            onChatOpen(chatId, currentUserId)
        }
    }

    Scaffold { paddingValues ->
        when (val state = userState) { // FIXED
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(
                        items = state.data.filter {
                            it.id.toString() != currentUserId
                        },
                        key = { it.id.toString() }
                    ) { user ->
                        UserItem(
                            user = user,
                            isLoading = chatState is UiState.Loading,
                            onClick = {
                                viewModel.openChat(user.id.toString())
                            }
                        )
                    }
                }
            }
        }

        if (chatState is UiState.Error) {
            Snackbar(
                modifier = Modifier.padding(16.dp)
            ) {
                Text((chatState as UiState.Error).message)
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserResponse,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        }
    }
}