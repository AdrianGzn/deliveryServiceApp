package com.alipoez.kt_demohilt.features.order.data.repositories

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.api.OrderApi
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderAssignDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderStatusUpdateDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.mapper.toDomain
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi
) : OrderRepository {

    override suspend fun createOrder(
        userId: Int,
        sellerId: Int,
        items: List<OrderItemRequestDTO>
    ): Order {
        val request = OrderRequestDTO(
            userId = userId,
            sellerId = sellerId,
            items = items
        )
        return orderApi.createOrder(request).toDomain()
    }

    override suspend fun getAllOrders(): List<Order> {
        return orderApi.getAllOrders().map { it.toDomain() }
    }

    override suspend fun getUserOrders(userId: Int): List<Order> {
        return orderApi.getUserOrders(userId).map { it.toDomain() }
    }

    override suspend fun getOrderById(orderId: Int): Order {
        return orderApi.getOrderById(orderId).toDomain()
    }

    override suspend fun updateOrderStatus(
        orderId: Int,
        status: String,
        userId: Int
    ): Order {
        val request = OrderStatusUpdateDTO(
            status = status,
            userId = userId
        )
        return orderApi.updateOrderStatus(orderId, request).toDomain()
    }

    override suspend fun assignDelivery(
        orderId: Int,
        deliveryId: Int
    ): Order {
        val request = OrderAssignDTO(deliveryId = deliveryId)
        return orderApi.assignDelivery(orderId, request).toDomain()
    }

    override suspend fun deleteOrder(orderId: Int) {
        orderApi.deleteOrder(orderId)
    }
}