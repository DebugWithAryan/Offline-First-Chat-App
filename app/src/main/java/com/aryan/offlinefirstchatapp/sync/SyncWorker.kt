package com.aryan.offlinefirstchatapp.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aryan.offlinefirstchatapp.data.remote.websocket.WebSocketClient
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import com.aryan.offlinefirstchatapp.domain.repository.MessageRepository

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: MessageRepository,
    private val webSocketClient: WebSocketClient
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        val pending = repository.getPendingMessages()

        return try {
            pending.forEach { message ->
                webSocketClient.sendMessage(message)
                repository.updateSyncStatus(message.id, SyncStatus.SENT)
            }
            Result.success()
        } catch (e: Exception){
            if (runAttemptCount< 3){
                Result.retry()
            } else {
                repository.updateSyncStatus(
                    pending.first().id,
                    SyncStatus.FAILED
                )
                Result.failure()
            }
        }
    }
}