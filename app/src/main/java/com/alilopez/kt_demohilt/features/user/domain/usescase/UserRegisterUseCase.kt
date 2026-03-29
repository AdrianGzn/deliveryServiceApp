package com.alilopez.kt_demohilt.features.user.domain.usescase

import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class UserRegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        password: String,
        role: String = "customer",
        address: String? = null,
        establishmentName: String? = null,
        establishmentAddress: String? = null
    ): User {
        return repository.register(
            name = name,
            password = password,
            role = role,
            address = address,
            establishmentName = establishmentName,
            establishmentAddress = establishmentAddress
        )
    }
}