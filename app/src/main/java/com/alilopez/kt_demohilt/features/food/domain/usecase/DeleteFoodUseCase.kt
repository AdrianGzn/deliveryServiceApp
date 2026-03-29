package com.alilopez.kt_demohilt.features.food.domain.usecase

import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import javax.inject.Inject

class DeleteFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteFood(id)
}