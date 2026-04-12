package com.alilopez.kt_demohilt.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.WebSocketManager
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.domain.usecase.AssignDeliveryUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.GetAllOrdersUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.GetUserOrdersUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.UpdateOrderStatusUseCase
import com.alilopez.kt_demohilt.features.order.presentation.status.DeliveryOrderUIState
import com.alilopez.kt_demohilt.features.order.presentation.status.NotificationType
import com.alilopez.kt_demohilt.features.order.presentation.status.OrderNotification
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
class DeliveryOrderViewModel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val getUserOrdersUseCase: GetUserOrdersUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val assignDeliveryUseCase: AssignDeliveryUseCase,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeliveryOrderUIState>(DeliveryOrderUIState.Loading)
    val uiState: StateFlow<DeliveryOrderUIState> = _uiState.asStateFlow()

    private var currentDeliveryId: Int = 0

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

    fun loadData(deliveryId: Int) {
        currentDeliveryId = deliveryId
        viewModelScope.launch {
            _uiState.update { DeliveryOrderUIState.Loading }
            try {
                val allOrders = getAllOrdersUseCase()
                val availableOrders = allOrders.filter {
                    it.status == "pending" && it.deliveryId == null
                }

                val myOrders = getUserOrdersUseCase(deliveryId)
                val assignedOrders = myOrders.filter { it.deliveryId == deliveryId }

                _uiState.update {
                    DeliveryOrderUIState.Success(
                        availableOrders = availableOrders,
                        myAssignedOrders = assignedOrders,
                        activeDelivery = assignedOrders.firstOrNull {
                            it.status != "delivered" && it.status != "cancelled"
                        }
                    )
                }

                webSocketManager.connect(deliveryId)

            } catch (e: Exception) {
                _uiState.update {
                    DeliveryOrderUIState.Error("Error al cargar datos: ${e.message}")
                }
            }
        }
    }

    fun acceptOrder(orderId: Int, deliveryId: Int) {
        viewModelScope.launch {
            try {
                assignDeliveryUseCase(
                    orderId = orderId,
                    deliveryId = deliveryId
                )

                addNotification(
                    OrderNotification(
                        id = UUID.randomUUID().toString(),
                        orderId = orderId,
                        message = "Pedido #$orderId aceptado",
                        type = NotificationType.ORDER_UPDATED
                    )
                )

                loadData(deliveryId)

            } catch (e: Exception) {
                handleError("Error al aceptar pedido: ${e.message}")
            }
        }
    }

    fun updateOrderStatus(orderId: Int, newStatus: String, deliveryId: Int) {
        viewModelScope.launch {
            try {
                updateOrderStatusUseCase(
                    orderId = orderId,
                    status = newStatus,
                    userId = deliveryId
                )

                val statusMessage = when (newStatus) {
                    "in_coming" -> "Has iniciado el viaje"
                    "arrived" -> "Has llegado al destino"
                    "delivered" -> "Pedido entregado"
                    else -> "Estado actualizado"
                }

                addNotification(
                    OrderNotification(
                        id = UUID.randomUUID().toString(),
                        orderId = orderId,
                        message = statusMessage,
                        type = NotificationType.ORDER_UPDATED
                    )
                )

                loadData(deliveryId)

            } catch (e: Exception) {
                handleError("Error al actualizar estado: ${e.message}")
            }
        }
    }

    private fun handleOrderUpdate(updatedOrder: Order) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is DeliveryOrderUIState.Success) {

                    val updatedAvailable = if (updatedOrder.status == "pending" && updatedOrder.deliveryId == null) {
                        val exists = currentState.availableOrders.any { it.id == updatedOrder.id }
                        if (exists) {
                            currentState.availableOrders.map {
                                if (it.id == updatedOrder.id) updatedOrder else it
                            }
                        } else {
                            currentState.availableOrders + updatedOrder
                        }
                    } else {
                        currentState.availableOrders.filter { it.id != updatedOrder.id }
                    }

                    val updatedAssigned = if (updatedOrder.deliveryId == currentDeliveryId) {
                        val exists = currentState.myAssignedOrders.any { it.id == updatedOrder.id }
                        if (exists) {
                            currentState.myAssignedOrders.map {
                                if (it.id == updatedOrder.id) updatedOrder else it
                            }
                        } else {
                            currentState.myAssignedOrders + updatedOrder
                        }
                    } else {
                        currentState.myAssignedOrders.filter { it.id != updatedOrder.id }
                    }

                    if (updatedOrder.deliveryId == currentDeliveryId) {
                        val notificationMessage = when (updatedOrder.status) {
                            "pickup" -> "Nuevo pedido asignado: ${updatedOrder.title}"
                            else -> "Pedido #${updatedOrder.id} actualizado a ${updatedOrder.statusDisplay}"
                        }

                        addNotification(
                            OrderNotification(
                                id = UUID.randomUUID().toString(),
                                orderId = updatedOrder.id,
                                message = notificationMessage,
                                type = NotificationType.ORDER_UPDATED
                            )
                        )
                    }

                    DeliveryOrderUIState.Success(
                        availableOrders = updatedAvailable,
                        myAssignedOrders = updatedAssigned,
                        activeDelivery = updatedAssigned.firstOrNull {
                            it.status != "delivered" && it.status != "cancelled"
                        },
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
                if (currentState is DeliveryOrderUIState.Success) {
                    DeliveryOrderUIState.Success(
                        availableOrders = currentState.availableOrders,
                        myAssignedOrders = currentState.myAssignedOrders,
                        activeDelivery = currentState.activeDelivery,
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
            if (currentState is DeliveryOrderUIState.Success) {
                DeliveryOrderUIState.Success(
                    availableOrders = currentState.availableOrders,
                    myAssignedOrders = currentState.myAssignedOrders,
                    activeDelivery = currentState.activeDelivery,
                    notifications = currentState.notifications.filter { it.id != notificationId }
                )
            } else {
                currentState
            }
        }
    }

    private fun handleError(message: String) {
        viewModelScope.launch {
            _uiState.update {
                DeliveryOrderUIState.Error(message)
            }

            delay(3000)
            currentDeliveryId?.let { loadData(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}