package com.alilopez.kt_demohilt.features.food.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.food.presentation.states.SellerHomeUIState
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
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SellerHomeUIState>(SellerHomeUIState.Loading)
    val uiState: StateFlow<SellerHomeUIState> = _uiState.asStateFlow()

    private var currentSellerId: Int = 0

    fun loadData(sellerId: Int) {
        currentSellerId = sellerId
        viewModelScope.launch {
            _uiState.update { SellerHomeUIState.Loading }
            try {
                // El vendedor ve sus productos (Food)
                val sellerFoods = foodRepository.getFoodBySeller(sellerId)
                _uiState.update { SellerHomeUIState.Success(foods = sellerFoods) }
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

    fun deleteProduct(foodId: Int) {
        viewModelScope.launch {
            try {
                foodRepository.deleteFood(foodId)
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