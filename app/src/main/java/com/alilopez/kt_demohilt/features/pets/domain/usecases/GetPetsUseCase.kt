package com.alilopez.kt_demohilt.features.pets.domain.usecases

import com.alilopez.kt_demohilt.features.pets.domain.repository.PetRepository
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val repository: PetRepository
) {
    suspend operator fun invoke() = repository.getPets()
}
