package com.alilopez.kt_demohilt.features.user.data.repositories

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.api.UserApi
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.mapper.toDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserCreateDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserLoginRequestDto
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {



    override suspend fun getUser(id: Int): User {
        return api.getUser(id).toDomain()
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        lastname: String,
        role: String,
        address: String?
    ): User {
        val userCreateDto = UserCreateDto(
            name = "$name $lastname",
            email = email,
            password = password,
            role = role,
            address = address
        )

        return api.register(userCreateDto).toDomain()
    }

    override suspend fun login(email: String, password: String): User {
        val loginRequest = UserLoginRequestDto(email = email, password = password)
        val loginResponse = api.login(loginRequest)
        return loginResponse.user?.toDomain()
            ?: throw Exception("Usuario no encontrado en la respuesta")
    }



    override suspend fun isUserLoggedIn(): Boolean {
        // TODO: Implementar lógica con DataStore
        return false
    }
}