package com.alilopez.kt_demohilt.features.user.domain.usescase


import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return userRepository.login(email, password)
    }
}