package com.alilopez.kt_demohilt.features.order.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderAssignDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderDetailResponseDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderResponseDTO
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderStatusUpdateDTO
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @POST("api/orders")
    @Headers("Content-Type: application/json")
    suspend fun createOrder(@Body request: OrderRequestDTO): OrderDetailResponseDTO

    @GET("api/orders")
    suspend fun getAllOrders(): List<OrderResponseDTO>

    @GET("api/orders/user/{userId}")
    suspend fun getUserOrders(@Path("userId") userId: Int): Response<List<OrderResponseDTO>>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): OrderDetailResponseDTO

    @PATCH("api/orders/{id}/status")
    @Headers("Content-Type: application/json")
    suspend fun updateOrderStatus(
        @Path("id") orderId: Int,
        @Body request: OrderStatusUpdateDTO
    ): OrderResponseDTO

    @POST("api/orders/{id}/assign")
    @Headers("Content-Type: application/json")
    suspend fun assignDelivery(
        @Path("id") orderId: Int,
        @Body request: OrderAssignDTO
    ): OrderResponseDTO

    @DELETE("api/orders/{id}")
    suspend fun deleteOrder(@Path("id") orderId: Int): Response<Unit>
}