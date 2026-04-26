package com.aryan.offlinefirstchatapp.ui.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.data.remote.api.AuthApiService
import com.aryan.offlinefirstchatapp.data.remote.api.UserResponse
import com.aryan.offlinefirstchatapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) : ViewModel(){
    private val _users = MutableStateFlow<UiState<List<UserResponse>>>(UiState.Loading)
    val users: StateFlow<UiState<List<UserResponse>>> = _users.asStateFlow()

    private val _chatState = MutableStateFlow<UiState<String>?>(null)
    val chatState: StateFlow<UiState<String>?> = _chatState.asStateFlow()

    val currentUserId= sessionManager.getUserId()

    init {
        loadUsers()
    }

    private fun loadUsers(){
        viewModelScope.launch {
            _users.value = UiState.Loading
            try {
                val response = authApiService.getUsers()
                if (response.isSuccessful){
                    _users.value = UiState.Success(response.body()!!)
                } else {
                    _users.value = UiState.Error("Failed to load users")
                }
            } catch (e: Exception){
                _users.value = UiState.Error("Network Error")
            }
        }
    }

    fun openChat(otherUserId: String){
        viewModelScope.launch {
            _chatState.value = UiState.Loading
            try {
                val response = authApiService.createChat(
                    CreateChatRequest(
                        userId1 = currentUserId,
                        userId2 = otherUserId
                    )
                )
                if (response.isSuccessful){
                    _chatState.value = UiState.Success(
                        response.body()!!.chatId
                    )
                } else {
                    _chatState.value = UiState.Error("Could not open chat")
                }
            } catch (e: Exception){
                _chatState.value = UiState.Error("Network Error")
            }
        }
    }
}