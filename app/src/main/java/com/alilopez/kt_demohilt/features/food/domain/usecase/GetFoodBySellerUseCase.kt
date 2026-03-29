package com.alilopez.kt_demohilt.features.food.domain.usecase

import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import javax.inject.Inject

class GetFoodBySellerUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(sellerId: Int) = repository.getFoodBySeller(sellerId)
}