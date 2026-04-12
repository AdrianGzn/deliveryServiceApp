package com.alilopez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class GetUserOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: Int): List<Order> {
        return repository.getUserOrders(userId = userId)
    }
}