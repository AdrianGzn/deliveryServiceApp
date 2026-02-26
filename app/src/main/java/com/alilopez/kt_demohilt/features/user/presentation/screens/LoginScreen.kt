package com.alilopez.kt_demohilt.features.user.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alilopez.kt_demohilt.core.components.Input
import com.alilopez.kt_demohilt.features.user.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToCustomer: () -> Unit,
    onNavigateToDelivery: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.user) {
        uiState.user?.let {
            when (it.role) {
                "customer" -> onNavigateToCustomer()
                "delivery" -> onNavigateToDelivery()
                else -> { /* No hacer nada */ }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Input(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = "Email",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(8.dp))

        Input(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = "Contraseña",
            isPassword = true,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.error != null) {
            Text(text = uiState.error!!, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.login() },
            enabled = !uiState.isLoading &&
                    uiState.email.isNotBlank() &&
                    uiState.password.isNotBlank()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Iniciar sesión")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToRegister) {
            Text(text = "¿No tienes una cuenta? Regístrate")
        }
    }
}