package com.alilopez.kt_demohilt.features.user.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.user.data.datasources.remote.model.UserDTO
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.entities.UserResponse

fun UserDTO.toDomain(): UserResponse {
    return UserResponse(
        id = this.id,
        name = this.name,
        role = this.role,
        address = this.address
    )
}

fun User.toDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        name = this.name,
        role = this.role,
        address = this.address
    )
}