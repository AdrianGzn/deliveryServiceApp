package com.alilopez.kt_demohilt.features.user.data.datasources.remote.model

data class UserDTO(
    val id: Int,
    val name: String,
    val role: String,
    val address: String? = null
)