package com.alilopez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        orderId: Int,
        status: String,
        userId: Int
    ): Order {
        return repository.updateOrderStatus(
            orderId = orderId,
            status = status,
            userId = userId
        )
    }
}