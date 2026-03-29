package com.alilopez.kt_demohilt.features.user.domain.entities

data class User(
    val id: Int,
    val name: String,
    val role: String,
    val password: String? = null,
    val address: String? = null,
    val establishmentName: String? = null,
    val establishmentAddress: String? = null
)

data class UserResponse(
    val id: Int,
    val name: String,
    val role: String,
    val address: String? = null,
    val establishmentName: String? = null,
    val establishmentAddress: String? = null
)