package com.alipoez.kt_demohilt.features.order.presentation.states

import com.alilopez.kt_demohilt.features.order.domain.entities.Order

sealed class CustomerOrderUIState {
    object Loading : CustomerOrderUIState()
    data class Success(
        val orders: List<Order>,
        val activeOrder: Order? = null,
        val notifications: List<OrderNotification> = emptyList()
    ) : CustomerOrderUIState()
    data class Error(val message: String) : CustomerOrderUIState()
}

data class OrderNotification(
    val id: String,
    val orderId: Int,
    val message: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis()
)

enum class NotificationType {
    ORDER_CREATED,
    ORDER_UPDATED,
    ORDER_DELIVERED,
    ORDER_CANCELLED,
    DELIVERY_ASSIGNED
}

data class CreateOrderRequest(
    val title: String = "",
    val description: String = "",
    val establishmentName: String = "",
    val establishmentAddress: String = "",
    val price: String = "",
    val isValid: Boolean = false
)