package com.aryan.offlinefirstchatapp.domain.usecase

import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.domain.repository.ChatConnectionRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val chatConnectionRepository: ChatConnectionRepository
) {
    operator fun invoke() {
        chatConnectionRepository.disconnect()
        sessionManager.clearSession()
    }
}