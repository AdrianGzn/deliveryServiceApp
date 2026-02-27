package com.alipoez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderStatusUpdateDTO(
    @SerializedName("status")
    val status: String,  // "pending", "pickup", "in_coming", "arrived", "delivered"

    @SerializedName("userId")
    val userId: Int
)