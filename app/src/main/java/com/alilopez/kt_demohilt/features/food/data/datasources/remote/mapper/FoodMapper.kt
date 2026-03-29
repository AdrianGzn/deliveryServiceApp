package com.alilopez.kt_demohilt.features.food.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.food.data.datasources.remote.model.FoodDTO
import com.alilopez.kt_demohilt.features.food.domain.entities.Food

fun FoodDTO.toDomain(): Food = Food(
    id = id,
    sellerId = sellerId,
    name = name,
    price = price
)

fun Food.toDTO(): FoodDTO = FoodDTO(
    id = id,
    sellerId = sellerId,
    name = name,
    price = price
)