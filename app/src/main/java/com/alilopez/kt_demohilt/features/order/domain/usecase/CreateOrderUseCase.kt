package com.alilopez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        userId: Int,
        sellerId: Int,
        items: List<OrderItemRequestDTO> = emptyList()
    ): Order {
        return repository.createOrder(
            userId = userId,
            sellerId = sellerId,
            items = items
        )
    }
}