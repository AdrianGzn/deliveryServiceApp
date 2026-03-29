package com.alilopez.kt_demohilt.features.food.data.di

import com.alilopez.kt_demohilt.features.food.data.datasources.remote.api.FoodApi
import com.alilopez.kt_demohilt.features.food.data.repositories.FoodRepositoryImpl
import com.alilopez.kt_demohilt.features.food.domain.repositories.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FoodModule {

    @Provides
    @Singleton
    fun provideFoodApi(retrofit: Retrofit): FoodApi {
        return retrofit.create(FoodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(foodApi: FoodApi): FoodRepository {
        return FoodRepositoryImpl(foodApi)
    }
}