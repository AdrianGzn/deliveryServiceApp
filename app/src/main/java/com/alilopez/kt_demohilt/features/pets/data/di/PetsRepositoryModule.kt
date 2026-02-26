package com.alilopez.kt_demohilt.features.pets.data.di

import com.alilopez.kt_demohilt.features.pets.data.repositories.PetRepositoryImpl
import com.alilopez.kt_demohilt.features.pets.domain.repository.PetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PetsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository
}