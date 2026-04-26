package com.aryan.offlinefirstchatapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aryan.offlinefirstchatapp.data.local.SessionManager
import com.aryan.offlinefirstchatapp.ui.auth.LoginScreen
import com.aryan.offlinefirstchatapp.ui.chat.components.ChatScreen

sealed class Screen(val route: String){
    object Login: Screen("login")
    object UserList: Screen("user_list")
    object Chat: Screen("chat/{chatId}/{userId}"){
        fun createRoute(chatId: String, userId: String) =
            "chat/$chatId/$userId"
    }
}

@Composable
fun AppNavigation(sessionManager: SessionManager) {

    val navController = rememberNavController()

    val startDestination = if (sessionManager.isLoggedIn())
        Screen.UserList.route
    else
        Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { _ ->
                    navController.navigate(Screen.UserList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.UserList.route) {
            UserListScreen(
                onChatOpen = { chatId, userId ->
                    navController.navigate(
                        Screen.Chat.createRoute(chatId, userId)
                    )
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ChatScreen(chatId = chatId, currentUserId = userId)
        }
    }
}