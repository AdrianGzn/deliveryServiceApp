package com.alilopez.kt_demohilt.features.pets.presentation

import com.alilopez.kt_demohilt.features.pets.domain.model.Pet

data class PetsUiState(
    val isLoading: Boolean = false,
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val error: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false
)