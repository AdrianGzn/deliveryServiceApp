package com.alilopez.kt_demohilt.features.user.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserCreateDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserLoginRequestDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserLoginResponseDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UsersResponse
import retrofit2.http.*

interface UserApi {

    @GET("user/getUsers")
    suspend fun getUsers(): UsersResponse

    @GET("user/getUser/{id}")
    suspend fun getUser(@Path("id") id: Int): UserDto

    @POST("user/register")
    suspend fun register(@Body user: UserCreateDto): UserDto

    @POST("user/login")
    suspend fun login(@Body loginRequest: UserLoginRequestDto): UserLoginResponseDto

    @PUT("user/updateUser/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): UserDto

    @DELETE("user/deleteUser/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}