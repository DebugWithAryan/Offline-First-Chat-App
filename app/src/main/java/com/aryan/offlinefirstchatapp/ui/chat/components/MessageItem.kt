package com.aryan.offlinefirstchatapp.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryan.offlinefirstchatapp.domain.model.Message
import com.aryan.offlinefirstchatapp.domain.model.SyncStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageItem(
    message: Message,
    currentUserId: String,
    onRetry: (String) -> Unit
) {
    val isOwn = message.senderId == currentUserId

    val timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Date(message.timestamp))

    val statusText = when (message.syncStatus) {
        SyncStatus.PENDING -> "⏱"
        SyncStatus.SENT -> "✓"
        SyncStatus.FAILED -> "✗ Tap to retry"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isOwn) 18.dp else 4.dp,
                            bottomEnd = if (isOwn) 4.dp else 18.dp
                        )
                    )
                    .background(
                        brush = if (isOwn)
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF7C83FD),
                                    Color(0xFF5B6BF8)
                                )
                            )
                        else
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.1f),
                                    Color.White.copy(alpha = 0.08f)
                                )
                            )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.content,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = if (message.syncStatus == SyncStatus.FAILED)
                    Modifier.clickable { onRetry(message.id) }
                else Modifier
            ) {
                Text(
                    text = timeFormatted,
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 10.sp
                )
                Text(
                    text = statusText,
                    fontSize = 10.sp,
                    color = when (message.syncStatus) {
                        SyncStatus.FAILED -> Color(0xFFFF6B6B)
                        SyncStatus.SENT -> Color(0xFF7C83FD)
                        SyncStatus.PENDING -> Color.White.copy(alpha = 0.35f)
                    },
                    fontWeight = if (message.syncStatus == SyncStatus.FAILED)
                        FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}