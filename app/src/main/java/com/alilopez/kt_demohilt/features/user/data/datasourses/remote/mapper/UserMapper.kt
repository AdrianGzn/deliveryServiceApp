package com.alilopez.kt_demohilt.features.user.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserDto
import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserLoginResponseDto
import com.alilopez.kt_demohilt.features.user.domain.entities.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        lastname = this.lastname,
        role = this.role ?: "user",
        address = this.address ?: ""
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        email = this.email,
        lastname = this.lastname,
        role = this.role,
        address = this.address
    )
}

fun UserLoginResponseDto.toDomain(): User? {
    return this.user?.toDomain()
}