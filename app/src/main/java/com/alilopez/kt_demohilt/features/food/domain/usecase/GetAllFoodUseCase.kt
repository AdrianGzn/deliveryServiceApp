package com.alilopez.kt_demohilt.features.food.domain.usecase

import com.alilopez.kt_demohilt.features.food.domain.entities.Food
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import javax.inject.Inject

class GetAllFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(): List<Food> {
        return repository.getAllFood()
    }
}