package com.aryan.offlinefirstchatapp.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aryan.offlinefirstchatapp.data.remote.api.ChatApiService
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketClient
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketMessage
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MessageRepository,
    private val chatApiService: ChatApiService
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {

        return try {
            val pending = repository.getPendingMessages()

            if (pending.isEmpty()) return Result.success()
            val response = chatApiService.syncMessages(
                pending.map { message ->
                    WebSocketMessage(
                        id = message.id,
                        chatId = message.chatId,
                        senderId = message.senderId,
                        content = message.content,
                        timestamp = message.timestamp
                    )
                }
            )
            if (response.isSuccessful){
                val body = response.body()!!
                body.syncedIds.forEach{ id ->
                    repository.updateSyncStatus(id, SyncStatus.SENT)
                }
                body.failedIds.forEach{ id ->
                    repository.updateSyncStatus(id, SyncStatus.FAILED)
                }
                Result.success()
            }else{
                Result.retry()
            }
        } catch (e: Exception){
            if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
    }
}