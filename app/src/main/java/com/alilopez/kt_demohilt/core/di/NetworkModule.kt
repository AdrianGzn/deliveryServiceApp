package com.alilopez.kt_demohilt.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @JsonPlaceHolderRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @PetsRetrofit
    fun providePetsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://54.158.229.20:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @DelivaryServiceRetrofit
    fun provideDeliveryServiceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://100.30.88.139:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}