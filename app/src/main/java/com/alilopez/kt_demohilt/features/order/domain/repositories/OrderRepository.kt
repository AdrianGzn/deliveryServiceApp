package com.alipoez.kt_demohilt.features.order.domain.repositories

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.order.domain.entities.Order


interface OrderRepository {

    suspend fun createOrder(
        userId: Int,
        sellerId: Int,
        items: List<OrderItemRequestDTO> = emptyList()
    ): Order

    suspend fun getAllOrders(): List<Order>

    suspend fun getUserOrders(userId: Int): List<Order>

    suspend fun getOrderById(orderId: Int): Order

    suspend fun updateOrderStatus(
        orderId: Int,
        status: String,
        userId: Int
    ): Order

    suspend fun assignDelivery(
        orderId: Int,
        deliveryId: Int
    ): Order

    suspend fun deleteOrder(orderId: Int)
}