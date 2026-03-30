package com.alilopez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDetailResponseDTO(
    @SerializedName("order")
    val order: OrderResponseDTO,

    @SerializedName("items")
    val items: List<OrderItemResponseDTO>
)

data class OrderItemResponseDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("orderId")
    val orderId: Int,

    @SerializedName("foodId")
    val foodId: Int,

    @SerializedName("foodName")
    val foodName: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double
)