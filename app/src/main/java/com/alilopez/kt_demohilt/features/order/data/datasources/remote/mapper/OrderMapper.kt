package com.alipoez.kt_demohilt.features.order.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderResponseDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO


fun OrderResponseDTO.toDomain(): Order {
    return Order(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        establishmentName = this.establishmentName,
        establishmentAddress = this.establishmentAddress,
        price = this.price,
        userId = this.userId,
        deliveryId = this.deliveryId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Order.toRequestDTO(): OrderRequestDTO {
    return OrderRequestDTO(
        title = this.title,
        description = this.description,
        establishmentName = this.establishmentName,
        establishmentAddress = this.establishmentAddress,
        price = this.price,
        userId = this.userId
    )
}