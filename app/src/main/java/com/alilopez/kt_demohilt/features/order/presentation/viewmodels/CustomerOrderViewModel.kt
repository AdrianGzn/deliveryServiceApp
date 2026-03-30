package com.alilopez.kt_demohilt.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.WebSocketManager
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import com.alipoez.kt_demohilt.features.order.presentation.states.CustomerOrderUIState
import com.alipoez.kt_demohilt.features.order.presentation.states.NotificationType
import com.alipoez.kt_demohilt.features.order.presentation.states.OrderNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CustomerOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<CustomerOrderUIState>(CustomerOrderUIState.Loading)
    val uiState: StateFlow<CustomerOrderUIState> = _uiState.asStateFlow()

    private var currentCustomerId: Int = 0

    init {
        viewModelScope.launch {
            observeWebSocket()
        }
    }

    private suspend fun observeWebSocket() {
        webSocketManager.orderUpdates.collect { updatedOrder ->
            handleOrderUpdate(updatedOrder)
        }
    }

    fun loadOrders(customerId: Int) {
        currentCustomerId = customerId
        viewModelScope.launch {
            _uiState.update { CustomerOrderUIState.Loading }
            try {
                val myOrders = orderRepository.getUserOrders(customerId)

                _uiState.update {
                    CustomerOrderUIState.Success(
                        orders = myOrders,
                        activeOrder = myOrders.firstOrNull { it.status != "delivered" && it.status != "cancelled" }
                    )
                }

                webSocketManager.connect(customerId)

            } catch (e: Exception) {
                _uiState.update {
                    CustomerOrderUIState.Error("Error al cargar pedidos: ${e.message}")
                }
            }
        }
    }

    fun cancelOrder(orderId: Int, customerId: Int) {
        viewModelScope.launch {
            try {
                orderRepository.updateOrderStatus(
                    orderId = orderId,
                    status = "cancelled",
                    userId = customerId
                )
                loadOrders(customerId)
            } catch (e: Exception) {
                handleError("Error al cancelar pedido: ${e.message}")
            }
        }
    }

    private fun handleOrderUpdate(updatedOrder: Order) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is CustomerOrderUIState.Success) {
                    val updatedOrders = currentState.orders.map { order ->
                        if (order.id == updatedOrder.id) updatedOrder else order
                    }

                    CustomerOrderUIState.Success(
                        orders = updatedOrders,
                        activeOrder = updatedOrders.firstOrNull { it.userId == currentCustomerId && it.status != "delivered" && it.status != "cancelled" },
                        notifications = currentState.notifications
                    )
                } else {
                    currentState
                }
            }
        }
    }

    private fun addNotification(notification: OrderNotification) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is CustomerOrderUIState.Success) {
                    CustomerOrderUIState.Success(
                        orders = currentState.orders,
                        activeOrder = currentState.activeOrder,
                        notifications = listOf(notification) + currentState.notifications
                    )
                } else {
                    currentState
                }
            }
            delay(5000)
            dismissNotification(notification.id)
        }
    }

    fun dismissNotification(notificationId: String) {
        _uiState.update { currentState ->
            if (currentState is CustomerOrderUIState.Success) {
                CustomerOrderUIState.Success(
                    orders = currentState.orders,
                    activeOrder = currentState.activeOrder,
                    notifications = currentState.notifications.filter { it.id != notificationId }
                )
            } else {
                currentState
            }
        }
    }

    private fun handleError(message: String) {
        viewModelScope.launch {
            _uiState.update { CustomerOrderUIState.Error(message) }
            delay(3000)
            loadOrders(currentCustomerId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}