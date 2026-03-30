package com.alilopez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderRequestDTO(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("sellerId")
    val sellerId: Int,

    @SerializedName("items")
    val items: List<OrderItemRequestDTO>
)

data class OrderItemRequestDTO(
    @SerializedName("foodId")
    val foodId: Int,
    @SerializedName("quantity")
    val quantity: Int
)