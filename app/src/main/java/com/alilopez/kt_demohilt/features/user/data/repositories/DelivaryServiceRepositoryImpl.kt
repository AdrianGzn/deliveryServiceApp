package com.alilopez.kt_demohilt.features.user.data.repositories

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.api.UserApi
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.LoginRequestDTO
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.RegisterRequestDTO
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.entities.UserResponse
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun register(
        name: String,
        password: String,
        role: String,
        address: String?,
        establishmentName: String?,
        establishmentAddress: String?
    ): User {
        val registerRequest = RegisterRequestDTO(
            name = name,
            password = password,
            role = role,
            address = address,
            establishmentName = establishmentName,
            establishmentAddress = establishmentAddress
        )

        Log.d("Register", "Enviando: $registerRequest")

        val userResponse = api.register(registerRequest).toDomain()

        return User(
            id = userResponse.id,
            name = userResponse.name,
            role = userResponse.role,
            password = "",
            address = userResponse.address,
            establishmentName = userResponse.establishmentName,
            establishmentAddress = userResponse.establishmentAddress
        )
    }

    override suspend fun login(name: String, password: String): User {
        val loginRequest = LoginRequestDTO(
            name = name,
            password = password
        )

        Log.d("Login", "Enviando: $loginRequest")

        val userResponse = api.login(loginRequest).toDomain()
        Log.d("Login", "Respuesta: $userResponse")

        return User(
            id = userResponse.id,
            name = userResponse.name,
            role = userResponse.role,
            password = "",
            address = userResponse.address,
            establishmentName = userResponse.establishmentName,
            establishmentAddress = userResponse.establishmentAddress
        )
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return false
    }
}