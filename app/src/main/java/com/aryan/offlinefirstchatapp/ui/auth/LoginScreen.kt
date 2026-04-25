package com.aryan.offlinefirstchatapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aryan.offlinefirstchatapp.ui.common.UiState

@Composable
fun LoginScreen(
    onLoginSuccess: (userId: String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            val userId = (authState as UiState.Success).data
            onLoginSuccess(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegistering) "Register" else "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authState is UiState.Loading && username.isNotEmpty()) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (isRegistering) viewModel.register(username, password)
                    else viewModel.login(username, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isRegistering) "Register" else "Login")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { isRegistering = !isRegistering }
        ) {
            Text(if (isRegistering) "Already have an account? Login" else "No account? Register")
        }

        if (authState is UiState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = (authState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}