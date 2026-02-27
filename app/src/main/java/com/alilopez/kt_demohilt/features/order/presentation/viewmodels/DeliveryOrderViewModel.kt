package com.alipoez.kt_demohilt.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.SSEManager
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.usecase.*
import com.alipoez.kt_demohilt.features.order.presentation.states.DeliveryOrderUIState
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
class DeliveryOrderViewModel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val getUserOrdersUseCase: GetUserOrdersUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val assignDeliveryUseCase: AssignDeliveryUseCase,
    private val sseManager: SSEManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeliveryOrderUIState>(DeliveryOrderUIState.Loading)
    val uiState: StateFlow<DeliveryOrderUIState> = _uiState.asStateFlow()

    private var currentDeliveryId: Int = 0

    init {
        viewModelScope.launch {
            initializeSSE()
        }
    }

    private suspend fun initializeSSE() {
        sseManager.connect(
            onOrderUpdate = { updatedOrder ->
                handleOrderUpdate(updatedOrder)
            },
            onOrderDeleted = { deletedOrderId ->
                handleOrderDeleted(deletedOrderId)
            },
            onError = { error ->
                handleError("Error en conexión SSE: $error")
            }
        )
    }

    fun loadData(deliveryId: Int) {
        currentDeliveryId = deliveryId
        viewModelScope.launch {
            _uiState.update { DeliveryOrderUIState.Loading }
            try {
                // Cargar todas las órdenes pendientes (disponibles)
                val allOrders = getAllOrdersUseCase()
                val availableOrders = allOrders.filter {
                    it.status == "pending" && it.deliveryId == null
                }

                // Cargar órdenes asignadas a este repartidor
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

                // Conectar SSE específico para este repartidor
                sseManager.connectToUser(deliveryId)

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
                val assignedOrder = assignDeliveryUseCase(
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

                // Recargar datos
                loadData(deliveryId)

            } catch (e: Exception) {
                handleError("Error al aceptar pedido: ${e.message}")
            }
        }
    }

    fun updateOrderStatus(orderId: Int, newStatus: String, deliveryId: Int) {
        viewModelScope.launch {
            try {
                val updatedOrder = updateOrderStatusUseCase(
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

                // Recargar datos
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

                    // Actualizar lista de disponibles
                    val updatedAvailable = if (updatedOrder.status == "pending" && updatedOrder.deliveryId == null) {
                        // Si la orden está pendiente y no tiene repartidor, actualizar o agregar a disponibles
                        val exists = currentState.availableOrders.any { it.id == updatedOrder.id }
                        if (exists) {
                            currentState.availableOrders.map {
                                if (it.id == updatedOrder.id) updatedOrder else it
                            }
                        } else {
                            currentState.availableOrders + updatedOrder
                        }
                    } else {
                        // Si ya no está disponible, quitarla de disponibles
                        currentState.availableOrders.filter { it.id != updatedOrder.id }
                    }

                    // Actualizar lista de asignados
                    val updatedAssigned = if (updatedOrder.deliveryId == currentDeliveryId) {
                        // Si esta orden es para este repartidor
                        val exists = currentState.myAssignedOrders.any { it.id == updatedOrder.id }
                        if (exists) {
                            currentState.myAssignedOrders.map {
                                if (it.id == updatedOrder.id) updatedOrder else it
                            }
                        } else {
                            currentState.myAssignedOrders + updatedOrder
                        }
                    } else {
                        // Si no es para este repartidor, quitarla de asignados
                        currentState.myAssignedOrders.filter { it.id != updatedOrder.id }
                    }

                    // Notificar si hay cambios relevantes
                    if (updatedOrder.deliveryId == currentDeliveryId) {
                        val notificationMessage = when (updatedOrder.status) {
                            "pickup" -> "Nuevo pedido asignado: ${updatedOrder.title}"
                            else -> "Pedido #${updatedOrder.id} actualizado a ${updatedOrder.status}"
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

    private fun handleOrderDeleted(deletedOrderId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is DeliveryOrderUIState.Success) {

                    addNotification(
                        OrderNotification(
                            id = UUID.randomUUID().toString(),
                            orderId = deletedOrderId,
                            message = "Pedido #$deletedOrderId ha sido cancelado",
                            type = NotificationType.ORDER_CANCELLED
                        )
                    )

                    DeliveryOrderUIState.Success(
                        availableOrders = currentState.availableOrders.filter { it.id != deletedOrderId },
                        myAssignedOrders = currentState.myAssignedOrders.filter { it.id != deletedOrderId },
                        activeDelivery = currentState.activeDelivery?.takeIf { it.id != deletedOrderId },
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
        viewModelScope.launch {
            sseManager.disconnect()
        }
    }
}