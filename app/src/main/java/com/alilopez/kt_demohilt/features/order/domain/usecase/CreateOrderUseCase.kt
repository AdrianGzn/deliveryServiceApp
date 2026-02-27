package com.alipoez.kt_demohilt.features.order.domain.usecase

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        establishmentName: String,
        establishmentAddress: String,
        price: Double,
        userId: Int
    ): Order {
        return repository.createOrder(
            title = title,
            description = description,
            establishmentName = establishmentName,
            establishmentAddress = establishmentAddress,
            price = price,
            userId = userId
        )
    }
}