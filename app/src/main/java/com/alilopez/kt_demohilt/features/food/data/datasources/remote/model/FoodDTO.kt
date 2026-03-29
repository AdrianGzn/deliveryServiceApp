package com.alilopez.kt_demohilt.features.food.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class FoodDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("sellerId") val sellerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double
)