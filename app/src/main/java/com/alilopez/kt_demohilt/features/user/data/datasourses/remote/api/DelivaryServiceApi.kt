package com.alilopez.kt_demohilt.features.user.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.LoginRequestDTO
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.RegisterRequestDTO
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserDTO
import retrofit2.http.*


interface UserApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequestDTO): UserDTO

    @POST("register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body request: RegisterRequestDTO): UserDTO
}
