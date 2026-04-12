package com.alilopez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int): Order {
        return repository.getOrderById(orderId = orderId)
    }
}