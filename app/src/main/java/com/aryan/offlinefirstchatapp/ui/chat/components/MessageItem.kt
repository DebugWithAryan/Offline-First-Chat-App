package com.aryan.offlinefirstchatapp.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus

@Composable
fun MessageItem(
    message: Message,
    currentUserId: String,
    onRetry: (String) -> Unit
){
    val isOwnMessage = message.senderId == currentUserId
    val statusText = when (message.syncStatus) {
        SyncStatus.PENDING -> "⏱ Sending"
        SyncStatus.SENT -> "✓ Sent"
        SyncStatus.FAILED -> "✗ Failed — tap to retry"
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isOwnMessage)
            Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isOwnMessage)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.content,
                color = if (isOwnMessage)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                color = if (message.syncStatus == SyncStatus.FAILED)
                    MaterialTheme.colorScheme.error
                else if (isOwnMessage)
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = if (message.syncStatus == SyncStatus.FAILED)
                    Modifier.clickable { onRetry(message.id) }
                else
                    Modifier
            )
        }
    }
}