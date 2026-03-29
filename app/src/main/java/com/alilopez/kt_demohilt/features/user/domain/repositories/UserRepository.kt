package com.alilopez.kt_demohilt.features.user.domain.repositories

import com.alilopez.kt_demohilt.features.user.domain.entities.User

interface UserRepository {
    suspend fun register(
        name: String,
        password: String,
        role: String,
        address: String? = null,
        establishmentName: String? = null,
        establishmentAddress: String? = null
    ): User


    suspend fun login(name: String, password: String): User

    suspend fun isUserLoggedIn(): Boolean
}