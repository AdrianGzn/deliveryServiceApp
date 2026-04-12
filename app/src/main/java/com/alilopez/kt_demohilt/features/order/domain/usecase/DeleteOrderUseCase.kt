package com.alilopez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int) {
        return repository.deleteOrder(orderId = orderId)
    }
}