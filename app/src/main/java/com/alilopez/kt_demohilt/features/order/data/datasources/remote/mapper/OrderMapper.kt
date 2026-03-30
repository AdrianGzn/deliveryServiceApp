package com.alipoez.kt_demohilt.features.order.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderDetailResponseDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderResponseDTO
import com.alilopez.kt_demohilt.features.order.domain.entities.Order

fun OrderResponseDTO.toDomain(): Order {
    return Order(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        price = this.price,
        userId = this.userId,
        sellerId = this.sellerId,
        deliveryId = this.deliveryId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun OrderDetailResponseDTO.toDomain(): Order {
    return Order(
        id = this.order.id,
        title = this.order.title,
        description = this.order.description,
        status = this.order.status,
        price = this.order.price,
        userId = this.order.userId,
        sellerId = this.order.sellerId,
        deliveryId = this.order.deliveryId,
        createdAt = this.order.createdAt,
        updatedAt = this.order.updatedAt
    )
}