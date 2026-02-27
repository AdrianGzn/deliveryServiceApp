package com.alilopez.kt_demohilt.features.user.presentation.screens

import com.alilopez.kt_demohilt.features.user.domain.entities.User

data class LoginUIState(
    val username: String = "",  // Añadido campo username
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)