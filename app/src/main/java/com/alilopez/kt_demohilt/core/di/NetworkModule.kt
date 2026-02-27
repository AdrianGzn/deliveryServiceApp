package com.alilopez.kt_demohilt.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @DelivaryServiceRetrofit
    fun provideDeliveryServiceRetrofit(
        okHttpClient: OkHttpClient  // Inyecta el OkHttpClient del módulo
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)  // Usa el mismo cliente
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}