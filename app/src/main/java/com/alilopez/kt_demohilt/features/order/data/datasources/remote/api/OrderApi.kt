package com.alilopez.kt_demohilt.features.order.data.datasources.remote.api

import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderAssignDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderResponseDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderStatusUpdateDTO
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    // Crear una nueva orden
    @POST("api/orders")
    @Headers("Content-Type: application/json")
    suspend fun createOrder(@Body request: OrderRequestDTO): OrderResponseDTO

    // Obtener todas las órdenes
    @GET("api/orders")
    suspend fun getAllOrders(): List<OrderResponseDTO>

    // Obtener órdenes de un usuario específico
    @GET("api/orders/user/{userId}")
    suspend fun getUserOrders(@Path("userId") userId: Int): List<OrderResponseDTO>

    // Obtener una orden por ID
    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): OrderResponseDTO

    // Actualizar estado de una orden
    @PATCH("api/orders/{id}/status")
    @Headers("Content-Type: application/json")
    suspend fun updateOrderStatus(
        @Path("id") orderId: Int,
        @Body request: OrderStatusUpdateDTO
    ): OrderResponseDTO

    // Asignar repartidor a una orden
    @POST("api/orders/{id}/assign")
    @Headers("Content-Type: application/json")
    suspend fun assignDelivery(
        @Path("id") orderId: Int,
        @Body request: OrderAssignDTO
    ): OrderResponseDTO

    // Eliminar una orden
    @DELETE("api/orders/{id}")
    suspend fun deleteOrder(@Path("id") orderId: Int): Response<Unit>
}