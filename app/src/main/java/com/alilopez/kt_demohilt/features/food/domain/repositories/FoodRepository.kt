package com.alilopez.kt_demohilt.features.food.domain.repositories

import com.alilopez.kt_demohilt.features.food.domain.entities.Food

interface FoodRepository {
    suspend fun createFood(food: Food): Food
    suspend fun getAllFood(): List<Food>
    suspend fun getFoodById(id: Int): Food
    suspend fun getFoodBySeller(sellerId: Int): List<Food>
    suspend fun updateFood(id: Int, food: Food): Food
    suspend fun deleteFood(id: Int)
}