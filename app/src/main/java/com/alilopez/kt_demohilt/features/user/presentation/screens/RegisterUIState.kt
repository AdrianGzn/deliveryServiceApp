package com.alilopez.kt_demohilt.features.user.presentation.screens

import com.alilopez.kt_demohilt.features.user.domain.entities.User

data class RegisterUIState(
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val user: User? = null,  // Añadido campo user
    val errorMessage: String? = null
)