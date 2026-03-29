package com.alilopez.kt_demohilt.features.food.presentation.states

import com.alilopez.kt_demohilt.features.order.domain.entities.Order

sealed class SellerHomeUIState {
    object Loading : SellerHomeUIState()
    data class Success(
        val orders: List<Order> = emptyList(),
        val isAddingOrder: Boolean = false,
        val errorMessage: String? = null
    ) : SellerHomeUIState()
    data class Error(val message: String) : SellerHomeUIState()
}