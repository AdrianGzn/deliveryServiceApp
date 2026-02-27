package com.alipoez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderRequestDTO(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("establishmentName")
    val establishmentName: String,

    @SerializedName("establishmentAddress")
    val establishmentAddress: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("userId")
    val userId: Int
)