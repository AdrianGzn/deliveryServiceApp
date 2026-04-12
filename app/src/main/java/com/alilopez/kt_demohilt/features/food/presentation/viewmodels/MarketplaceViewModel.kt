package com.alilopez.kt_demohilt.features.food.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.food.domain.usecase.GetAllFoodUseCase
import com.alilopez.kt_demohilt.features.food.presentation.states.MarketplaceUIState
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alilopez.kt_demohilt.features.order.domain.usecase.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val getAllFoodUseCase: GetAllFoodUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MarketplaceUIState>(MarketplaceUIState.Loading)
    val uiState: StateFlow<MarketplaceUIState> = _uiState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { MarketplaceUIState.Loading }
            try {
                val products = getAllFoodUseCase()
                _uiState.update { MarketplaceUIState.Success(products = products) }
            } catch (e: Exception) {
                _uiState.update { MarketplaceUIState.Error(e.message ?: "Error al cargar productos") }
            }
        }
    }

    fun buyProduct(userId: Int, sellerId: Int, foodId: Int) {
        viewModelScope.launch {
            try {
                val items = listOf(OrderItemRequestDTO(foodId = foodId, quantity = 1))
                createOrderUseCase(userId = userId, sellerId = sellerId, items = items)
                
                val currentState = _uiState.value
                if (currentState is MarketplaceUIState.Success) {
                    _uiState.update { currentState.copy(message = "¡Pedido realizado con éxito!") }
                }
            } catch (e: Exception) {
                val currentState = _uiState.value
                if (currentState is MarketplaceUIState.Success) {
                    _uiState.update { currentState.copy(message = "Error: ${e.message}") }
                }
            }
        }
    }
    
    fun clearMessage() {
        val currentState = _uiState.value
        if (currentState is MarketplaceUIState.Success) {
            _uiState.update { currentState.copy(message = null) }
        }
    }
}