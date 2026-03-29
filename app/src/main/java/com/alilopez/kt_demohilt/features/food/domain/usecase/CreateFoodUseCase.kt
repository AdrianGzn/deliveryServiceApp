package com.alilopez.kt_demohilt.features.food.domain.usecase

import com.alilopez.kt_demohilt.features.food.domain.entities.Food
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import javax.inject.Inject

class CreateFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(food: Food) = repository.createFood(food)
}