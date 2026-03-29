package com.alilopez.kt_demohilt.features.food.data.repositories

import com.alilopez.kt_demohilt.features.food.data.datasources.remote.api.FoodApi
import com.alilopez.kt_demohilt.features.food.data.datasources.remote.mapper.toDTO
import com.alilopez.kt_demohilt.features.food.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.food.domain.entities.Food
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val api: FoodApi
) : FoodRepository {
    override suspend fun createFood(food: Food): Food {
        return api.createFood(food.toDTO()).toDomain()
    }

    override suspend fun getAllFood(): List<Food> {
        return api.getAllFood().map { it.toDomain() }
    }

    override suspend fun getFoodById(id: Int): Food {
        return api.getFoodById(id).toDomain()
    }

    override suspend fun getFoodBySeller(sellerId: Int): List<Food> {
        return api.getFoodBySeller(sellerId).map { it.toDomain() }
    }

    override suspend fun updateFood(id: Int, food: Food): Food {
        return api.updateFood(id, food.toDTO()).toDomain()
    }

    override suspend fun deleteFood(id: Int) {
        api.deleteFood(id)
    }
}