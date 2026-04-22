package com.aryan.offlinefirstchatapp.domain.usecase

import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository
import javax.inject.Inject

class SaveIncomingMessageUseCase @Inject constructor(
    private val repository: MessageRepository
){
    suspend operator fun invoke(message: Message){
        repository.sendMessage(message)
    }
}