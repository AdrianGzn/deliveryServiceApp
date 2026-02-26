package com.alilopez.kt_demohilt.features.user.domain.entities


data class User(
    val id: Int,
    val name: String,
    val email: String,
    val lastname: String,
    val role: String,
    val address: String?
)