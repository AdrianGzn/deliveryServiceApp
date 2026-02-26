package com.alilopez.kt_demohilt.features.user.data.datasources.remote.model

data class LoginResponseDTO(
    val token: String? = null,  // Si el backend devuelve token
    val user: UserDTO  // El usuario sin contraseña
)