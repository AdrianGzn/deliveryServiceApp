package com.alilopez.kt_demohilt.features.user.domain.usescase


import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository

class UserRegisterUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        name: String,
        lastname: String,
        password: String
    ): User {
        return userRepository.register(
            name = name,
            email = email,
            password = password,
            lastname = lastname,
            role = "user",
            address = null
        )
    }
}