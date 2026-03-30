package com.alilopez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderAssignDTO(
    @SerializedName("deliveryId")
    val deliveryId: Int
)