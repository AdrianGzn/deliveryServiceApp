package com.alipoez.kt_demohilt.features.order.domain.repositories

import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO


interface OrderRepository {

    // Crear una nueva orden
    suspend fun createOrder(
        title: String,
        description: String,
        establishmentName: String,
        establishmentAddress: String,
        price: Double,
        userId: Int,
        sellerId: Int,
        items: List<OrderItemRequestDTO> = emptyList()
    ): Order

    // Obtener todas las órdenes
    suspend fun getAllOrders(): List<Order>

    // Obtener órdenes de un usuario específico
    suspend fun getUserOrders(userId: Int): List<Order>

    // Obtener una orden por ID
    suspend fun getOrderById(orderId: Int): Order

    // Actualizar estado de una orden
    suspend fun updateOrderStatus(
        orderId: Int,
        status: String,
        userId: Int
    ): Order

    // Asignar repartidor a una orden
    suspend fun assignDelivery(
        orderId: Int,
        deliveryId: Int
    ): Order

    // Eliminar una orden
    suspend fun deleteOrder(orderId: Int)
}