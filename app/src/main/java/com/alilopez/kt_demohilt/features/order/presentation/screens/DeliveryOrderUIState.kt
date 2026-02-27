package com.alipoez.kt_demohilt.features.order.presentation.states

import com.alilopez.kt_demohilt.features.order.domain.entities.Order

sealed class DeliveryOrderUIState {
    object Loading : DeliveryOrderUIState()
    data class Success(
        val availableOrders: List<Order> = emptyList(),
        val myAssignedOrders: List<Order> = emptyList(),
        val activeDelivery: Order? = null,
        val notifications: List<OrderNotification> = emptyList()
    ) : DeliveryOrderUIState()
    data class Error(val message: String) : DeliveryOrderUIState()
}

data class DeliveryStats(
    val totalDeliveries: Int = 0,
    val completedToday: Int = 0,
    val totalEarned: Double = 0.0
)