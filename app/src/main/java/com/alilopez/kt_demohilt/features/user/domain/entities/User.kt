package com.alilopez.kt_demohilt.features.user.domain.entities


data class User(
    val id: Int,
    val name: String,
    val role: String,
    val password: String,
    val address: String?
)

data class UserResponse(
    val id: Int,
    val name: String,
    val role: String,
    val address: String?
)