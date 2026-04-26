package com.aryan.offlinefirstchatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.ui.auth.LoginScreen
import com.aryan.offlinefirstchatapp.ui.chat.components.ChatScreen
import com.aryan.offlinefirstchatapp.ui.navigation.AppNavigation
import com.aryan.offlinefirstchatapp.ui.theme.OfflineFirstChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineFirstChatAppTheme {
                AppNavigation(sessionManager = sessionManager)
            }
        }
    }
}
