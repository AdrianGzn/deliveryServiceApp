package com.alilopez.kt_demohilt.features.pets.data.repositories

import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.api.PetsApi
import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.mapper.toDTO
import com.alilopez.kt_demohilt.features.pets.domain.model.Pet
import com.alilopez.kt_demohilt.features.pets.domain.repository.PetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepositoryImpl @Inject constructor(
    private val api: PetsApi
) : PetRepository {

    override suspend fun getPets(): List<Pet> {
        return api.getPets().map { it.toDomain() }
    }

    override suspend fun getPetById(id: Int): Pet {
        return api.getPetById(id).toDomain()
    }

    override suspend fun createPet(pet: Pet): Pet {
        return api.createPet(pet.toDTO()).toDomain()
    }

    override suspend fun updatePet(id: Int, pet: Pet): Pet {
        return api.updatePet(id, pet.toDTO()).toDomain()
    }

    override suspend fun deletePet(id: Int) {
        api.deletePet(id)
    }
}