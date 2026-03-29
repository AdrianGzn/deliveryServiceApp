package com.alilopez.kt_demohilt.features.food.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.managers.WebSocketManager
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import com.alilopez.kt_demohilt.features.food.presentation.states.SellerHomeUIState
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import com.alilopez.kt_demohilt.features.food.domain.entities.Food
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
                if (updatedOrder.sellerId == currentSellerId) {
                    loadOrders(currentSellerId)
                }
            }
        }
    }

    fun loadOrders(sellerId: Int) {
        currentSellerId = sellerId
        viewModelScope.launch {
            _uiState.update { SellerHomeUIState.Loading }
            try {
                // Obtenemos todas y filtraremos por nuestro sellerId (según README punto 5)
                val allOrders = orderRepository.getAllOrders()
                val sellerOrders = allOrders.filter { it.sellerId == sellerId }
                _uiState.update { SellerHomeUIState.Success(orders = sellerOrders) }
            } catch (e: Exception) {
                _uiState.update { SellerHomeUIState.Error(e.message ?: "Error al cargar órdenes") }
            }
        }
    }

    fun createOrder(title: String, description: String, price: Double) {
        viewModelScope.launch {
            try {
                // 1. Primero creamos el alimento en el catálogo para obtener un ID válido
                val newFood = foodRepository.createFood(
                    Food(id = 0, sellerId = currentSellerId, name = title, price = price)
                )
                
                // 2. Ahora creamos la orden usando el ID real del alimento creado
                val items = listOf(OrderItemRequestDTO(foodId = newFood.id, quantity = 1))
                
                orderRepository.createOrder(
                    title = title,
                    description = description,
                    establishmentName = "Mi Tienda",
                    establishmentAddress = "Dirección Tienda",
                    price = price,
                    userId = 0,
                    sellerId = currentSellerId,
                    items = items
                )
                loadOrders(currentSellerId)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun updateStatus(orderId: Int, newStatus: String) {
        viewModelScope.launch {
            try {
                orderRepository.updateOrderStatus(orderId, newStatus, currentSellerId)
                loadOrders(currentSellerId)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        val currentState = _uiState.value
        if (currentState is SellerHomeUIState.Success) {
            _uiState.update { currentState.copy(errorMessage = e.message) }
        }
    }
}