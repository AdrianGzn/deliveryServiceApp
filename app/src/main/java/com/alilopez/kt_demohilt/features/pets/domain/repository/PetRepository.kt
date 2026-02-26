package com.alilopez.kt_demohilt.features.pets.domain.repository

import com.alilopez.kt_demohilt.features.pets.domain.model.Pet

interface PetRepository {
    suspend fun getPets(): List<Pet>
    suspend fun getPetById(id: Int): Pet
    suspend fun createPet(pet: Pet): Pet
    suspend fun updatePet(id: Int, pet: Pet): Pet
    suspend fun deletePet(id: Int)
}