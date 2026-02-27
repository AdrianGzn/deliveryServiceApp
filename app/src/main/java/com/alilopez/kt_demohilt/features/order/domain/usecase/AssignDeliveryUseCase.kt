package com.alipoez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class AssignDeliveryUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        orderId: Int,
        deliveryId: Int
    ): Order {
        return repository.assignDelivery(
            orderId = orderId,
            deliveryId = deliveryId
        )
    }
}