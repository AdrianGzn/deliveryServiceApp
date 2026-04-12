package com.alilopez.kt_demohilt.features.food.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.WebSocketManager
import com.alilopez.kt_demohilt.features.food.presentation.states.SellerHomeUIState
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import com.alilopez.kt_demohilt.features.food.domain.entities.Food
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val foodRepository: FoodRepository,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<SellerHomeUIState>(SellerHomeUIState.Loading)
    val uiState: StateFlow<SellerHomeUIState> = _uiState.asStateFlow()

    private var currentSellerId: Int = 0

    init {
        observeWebSocket()
    }

    private fun observeWebSocket() {
        viewModelScope.launch {
            webSocketManager.orderUpdates.collect { updatedOrder ->
                // Si llega una actualización de orden para este vendedor, refrescamos la lista de órdenes
                if (updatedOrder.sellerId == currentSellerId) {
                    loadData(currentSellerId)
                }
            }
        }
    }

    fun loadData(sellerId: Int) {
        currentSellerId = sellerId
        viewModelScope.launch {
            _uiState.update { SellerHomeUIState.Loading }
            try {
                // Según el back: El vendedor ve sus órdenes filtrando la lista general
                val allOrders = orderRepository.getAllOrders()
                val sellerOrders = allOrders.filter { it.sellerId == sellerId && it.userId != 0 }
                
                _uiState.update { SellerHomeUIState.Success(orders = sellerOrders) }
                
                // Conectamos el WebSocket del vendedor para recibir actualizaciones de sus pedidos
                webSocketManager.connect(sellerId)
            } catch (e: Exception) {
                _uiState.update { SellerHomeUIState.Error(e.message ?: "Error al cargar datos") }
            }
        }
    }

    fun createProduct(foodName: String, foodPrice: Double) {
        viewModelScope.launch {
            try {
                foodRepository.createFood(
                    Food(id = 0, sellerId = currentSellerId, name = foodName, price = foodPrice)
                )
                // Después de crear un producto, refrescamos los datos
                loadData(currentSellerId)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun updateStatus(orderId: Int, newStatus: String) {
        viewModelScope.launch {
            try {
                orderRepository.updateOrderStatus(orderId, newStatus, currentSellerId)
                loadData(currentSellerId)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        val currentState = _uiState.value
        if (currentState is SellerHomeUIState.Success) {
            _uiState.update { currentState.copy(errorMessage = e.message) }
        } else {
            _uiState.update { SellerHomeUIState.Error(e.message ?: "Error desconocido") }
        }
    }
}