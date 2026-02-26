package com.alilopez.kt_demohilt.features.user.domain.repositories

import com.alilopez.kt_demohilt.features.user.domain.entities.User

interface UserRepository {
    // Agregar email y lastname a los parámetros
    suspend fun register(
        name: String,
        email: String,      // Agregar
        password: String,
        lastname: String,   // Agregar
        role: String,
        address: String?
    ): User

    // Cambiar de "name" a "email"
    suspend fun login(email: String, password: String): User

    suspend fun getUser(id: Int): User
    suspend fun isUserLoggedIn(): Boolean
}