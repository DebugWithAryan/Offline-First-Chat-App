package com.aryan.offlinefirstchatapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.data.remote.api.AuthApiService
import com.aryan.offlinefirstchatapp.data.remote.api.LoginRequest
import com.aryan.offlinefirstchatapp.data.remote.api.RegisterRequest
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketClient
import com.aryan.offlinefirstchatapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager,
    private val webSocketClient: WebSocketClient
): ViewModel() {
    private val _authState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val authState: StateFlow<UiState<String>> = _authState.asStateFlow()

    fun login(username: String, password: String){
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val response = authApiService.login(
                    LoginRequest(username, password)
                )
                if(response.isSuccessful){
                    val body = response.body()!!
                    sessionManager.saveSession(body.userId, body.token)
                    _authState.value = UiState.Success(body.userId)
                } else {
                    _authState.value = UiState.Error("Invalid Credentials")
                }
            } catch (e: Exception){
                _authState.value = UiState.Error("Network Error")
            }
        }
    }

    fun register(username: String, password: String){
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val response = authApiService.register(
                    RegisterRequest(username, password)
                )
                if (response.isSuccessful){
                    val body = response.body()!!
                    sessionManager.saveSession(body.userId, body.token)
                    _authState.value = UiState.Success(body.userId)
                } else if (response.code()==409){
                    _authState.value = UiState.Error("Username already taken")
                } else {
                    _authState.value = UiState.Error("Registration Failed")
                }
            } catch (e: Exception){
                _authState.value = UiState.Error("Network Error")
            }
        }
    }
}