package com.alilopez.kt_demohilt.features.user.data.datasources.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val data: List<UserDto>
)

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val name: String,
    val lastname: String,
    val role: String? = null,
    val address: String? = null
)

@Serializable
data class UserCreateDto(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "user",
    val address: String? = null
)

@Serializable
data class UserLoginResponseDto(
    val access_token: String,
    val token_type: String,
    val user: UserDto?
)

@Serializable
data class UserLoginRequestDto(
    val email: String,
    val password: String
)