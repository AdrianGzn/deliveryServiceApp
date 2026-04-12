package com.alilopez.kt_demohilt.features.food.presentation.states

import com.alilopez.kt_demohilt.features.food.domain.entities.Food

sealed class MarketplaceUIState {
    object Loading : MarketplaceUIState()
    data class Success(
        val products: List<Food> = emptyList(),
        val message: String? = null
    ) : MarketplaceUIState()
    data class Error(val message: String) : MarketplaceUIState()
}