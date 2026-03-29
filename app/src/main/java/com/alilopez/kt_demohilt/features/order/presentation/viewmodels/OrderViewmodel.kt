package com.alilopez.kt_demohilt.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.api.OrderApi
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.mapper.toDomain
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderItemRequestDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderRequestDTO
import com.alipoez.kt_demohilt.features.order.data.datasources.remote.model.OrderStatusUpdateDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



class OrderViewmodel @Inject constructor(
    private val orderApi: OrderApi
) : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun createOrder(
        title: String,
        description: String,
        establishmentName: String,
        establishmentAddress: String,
        price: Double,
        userId: Int,
        sellerId: Int,
        items: List<OrderItemRequestDTO> = emptyList()
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = OrderRequestDTO(
                    title = title,
                    description = description,
                    establishmentName = establishmentName,
                    establishmentAddress = establishmentAddress,
                    price = price,
                    userId = userId,
                    sellerId = sellerId,
                    items = items
                )
                val response = orderApi.createOrder(request)
                // Actualizar UI
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateOrderStatus(orderId: Int, status: String, userId: Int) {
        viewModelScope.launch {
            try {
                val request = OrderStatusUpdateDTO(status = status, userId = userId)
                val updatedOrder = orderApi.updateOrderStatus(orderId, request)
                // Actualizar la orden en la lista
                _orders.update { orders ->
                    orders.map { if (it.id == orderId) updatedOrder.toDomain() else it }
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}