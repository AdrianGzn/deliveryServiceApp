package com.alilopez.kt_demohilt.features.food.domain.entities

data class Food(
    val id: Int,
    val sellerId: Int,
    val name: String,
    val price: Double
)