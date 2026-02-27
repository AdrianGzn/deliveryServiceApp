package com.alipoez.kt_demohilt.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.SSEManager
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.usecase.*
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
    private val createOrderUseCase: CreateOrderUseCase,
    private val getUserOrdersUseCase: GetUserOrdersUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val sseManager: SSEManager  // Necesitaremos crear este manager
) : ViewModel() {

    private val _uiState = MutableStateFlow<CustomerOrderUIState>(CustomerOrderUIState.Loading)
    val uiState: StateFlow<CustomerOrderUIState> = _uiState.asStateFlow()

    private var currentCustomerId: Int = 0

    init {
        // Iniciar conexión SSE cuando se crea el ViewModel
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

    fun loadOrders(customerId: Int) {
        currentCustomerId = customerId
        viewModelScope.launch {
            _uiState.update { CustomerOrderUIState.Loading }
            try {
                val orders = getUserOrdersUseCase(customerId)
                _uiState.update {
                    CustomerOrderUIState.Success(
                        orders = orders,
                        activeOrder = orders.firstOrNull { it.status != "delivered" && it.status != "cancelled" }
                    )
                }

                // Conectar SSE específico para este cliente
                sseManager.connectToUser(customerId)

            } catch (e: Exception) {
                _uiState.update {
                    CustomerOrderUIState.Error("Error al cargar pedidos: ${e.message}")
                }
            }
        }
    }

    fun createOrder(
        customerId: Int,
        title: String,
        description: String,
        establishmentName: String,
        establishmentAddress: String,
        price: Double
    ) {
        viewModelScope.launch {
            try {
                val newOrder = createOrderUseCase(
                    title = title,
                    description = description,
                    establishmentName = establishmentName,
                    establishmentAddress = establishmentAddress,
                    price = price,
                    userId = customerId
                )

                addNotification(
                    OrderNotification(
                        id = UUID.randomUUID().toString(),
                        orderId = newOrder.id,
                        message = "Pedido creado exitosamente",
                        type = NotificationType.ORDER_CREATED
                    )
                )

                // Recargar órdenes
                loadOrders(customerId)

            } catch (e: Exception) {
                addNotification(
                    OrderNotification(
                        id = UUID.randomUUID().toString(),
                        orderId = 0,
                        message = "Error al crear pedido: ${e.message}",
                        type = NotificationType.ORDER_UPDATED
                    )
                )
            }
        }
    }

    fun cancelOrder(orderId: Int, customerId: Int) {
        viewModelScope.launch {
            try {
                // En tu backend, cancelar una orden podría ser actualizar el estado a "cancelled"
                updateOrderStatusUseCase(
                    orderId = orderId,
                    status = "cancelled",
                    userId = customerId
                )

                addNotification(
                    OrderNotification(
                        id = UUID.randomUUID().toString(),
                        orderId = orderId,
                        message = "Pedido cancelado",
                        type = NotificationType.ORDER_CANCELLED
                    )
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

                    // Añadir notificación según el nuevo estado
                    val notificationMessage = when (updatedOrder.status) {
                        "pickup" -> "Tu pedido está listo para recoger"
                        "in_coming" -> "El repartidor está en camino"
                        "arrived" -> "El repartidor ha llegado"
                        "delivered" -> "¡Tu pedido ha sido entregado!"
                        else -> null
                    }

                    notificationMessage?.let {
                        addNotification(
                            OrderNotification(
                                id = UUID.randomUUID().toString(),
                                orderId = updatedOrder.id,
                                message = it,
                                type = when (updatedOrder.status) {
                                    "delivered" -> NotificationType.ORDER_DELIVERED
                                    else -> NotificationType.ORDER_UPDATED
                                }
                            )
                        )
                    }

                    // Si se asignó un repartidor
                    if (updatedOrder.deliveryId != null &&
                        currentState.orders.find { it.id == updatedOrder.id }?.deliveryId == null) {
                        addNotification(
                            OrderNotification(
                                id = UUID.randomUUID().toString(),
                                orderId = updatedOrder.id,
                                message = "Se ha asignado un repartidor a tu pedido",
                                type = NotificationType.DELIVERY_ASSIGNED
                            )
                        )
                    }

                    CustomerOrderUIState.Success(
                        orders = updatedOrders,
                        activeOrder = updatedOrders.firstOrNull { it.status != "delivered" && it.status != "cancelled" },
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
                if (currentState is CustomerOrderUIState.Success) {
                    val filteredOrders = currentState.orders.filter { it.id != deletedOrderId }

                    addNotification(
                        OrderNotification(
                            id = UUID.randomUUID().toString(),
                            orderId = deletedOrderId,
                            message = "El pedido ha sido eliminado",
                            type = NotificationType.ORDER_CANCELLED
                        )
                    )

                    CustomerOrderUIState.Success(
                        orders = filteredOrders,
                        activeOrder = filteredOrders.firstOrNull { it.status != "delivered" && it.status != "cancelled" },
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

            // Auto-dismiss después de 5 segundos
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
            _uiState.update {
                CustomerOrderUIState.Error(message)
            }

            // Auto-limpiar error después de 3 segundos
            delay(3000)
            currentCustomerId?.let { loadOrders(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            sseManager.disconnect()
        }
    }
}