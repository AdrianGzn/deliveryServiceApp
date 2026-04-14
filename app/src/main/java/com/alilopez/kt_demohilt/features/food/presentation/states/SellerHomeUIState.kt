package com.alilopez.kt_demohilt.features.food.presentation.states

import com.alilopez.kt_demohilt.features.food.domain.entities.Food

sealed class SellerHomeUIState {
    object Loading : SellerHomeUIState()
    data class Success(
        val foods: List<Food> = emptyList(),
        val isAddingFood: Boolean = false,
        val errorMessage: String? = null
    ) : SellerHomeUIState()
    data class Error(val message: String) : SellerHomeUIState()
}