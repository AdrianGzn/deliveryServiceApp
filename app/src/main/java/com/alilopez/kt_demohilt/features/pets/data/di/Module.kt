package com.alilopez.kt_demohilt.features.pets.data.di

import com.alilopez.kt_demohilt.core.di.PetsRetrofit
import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.api.PetsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PetsNetworkModule {

    @Provides
    @Singleton
    fun providePetsApi(@PetsRetrofit retrofit: Retrofit): PetsApi {
        return retrofit.create(PetsApi::class.java)
    }
}