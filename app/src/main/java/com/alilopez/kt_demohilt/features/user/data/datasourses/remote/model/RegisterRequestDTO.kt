package com.alilopez.kt_demohilt.features.user.data.datasources.remote.model



data class RegisterRequestDTO(
    val name: String,
    val password: String,
    val role: String,
    val address: String? = null
)