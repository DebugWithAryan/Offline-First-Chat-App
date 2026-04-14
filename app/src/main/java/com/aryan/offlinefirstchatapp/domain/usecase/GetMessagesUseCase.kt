package com.aryan.offlinefirstchatapp.domain.usecase

import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>>{
        return repository.getMessages(chatId)
    }
}