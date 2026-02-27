package com.alipoez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): List<Order> {
        return repository.getAllOrders()
    }
}