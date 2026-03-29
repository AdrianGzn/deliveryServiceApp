package com.alipoez.kt_demohilt.features.order.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class OrderResponseDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("establishmentName")
    val establishmentName: String?, // Cambiado a nullable

    @SerializedName("establishmentAddress")
    val establishmentAddress: String?, // Cambiado a nullable

    @SerializedName("price")
    val price: Double,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("sellerId")
    val sellerId: Int,

    @SerializedName("deliveryId")
    val deliveryId: Int?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)