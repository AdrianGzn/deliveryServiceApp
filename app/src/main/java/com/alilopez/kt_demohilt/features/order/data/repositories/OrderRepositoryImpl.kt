package com.alilopez.kt_demohilt.features.order.data.repositories

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.api.OrderApi
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderAssignDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderStatusUpdateDTO
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
        val response = orderApi.createOrder(request)
        return response.toDomain()
    }

    override suspend fun getAllOrders(): List<Order> {
        return orderApi.getAllOrders().map { it.toDomain() }
    }

    override suspend fun getUserOrders(userId: Int): List<Order> {
        val response = orderApi.getUserOrders(userId)
        if (response.isSuccessful) {
            return response.body()?.map { it.toDomain() } ?: emptyList()
        }
        return emptyList()
    }

    override suspend fun getOrderById(orderId: Int): Order {
        val response = orderApi.getOrderById(orderId)
        return response.toDomain()
    }

    override suspend fun updateOrderStatus(orderId: Int, status: String, userId: Int): Order {
        val request = OrderStatusUpdateDTO(
            status = status,
            userId = userId
        )
        val response = orderApi.updateOrderStatus(orderId, request)
        return response.toDomain()
    }

    override suspend fun assignDelivery(orderId: Int, deliveryId: Int): Order {
        val request = OrderAssignDTO(deliveryId = deliveryId)
        val response = orderApi.assignDelivery(orderId, request)
        return response.toDomain()
    }

    override suspend fun deleteOrder(orderId: Int) {
        orderApi.deleteOrder(orderId)
    }
}