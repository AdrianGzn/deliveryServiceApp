package com.alilopez.kt_demohilt.features.order.data.di

import com.alilopez.kt_demohilt.core.di.DelivaryServiceRetrofit
import com.alilopez.kt_demohilt.features.order.data.datasources.remote.api.OrderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderNetworkModule {

    @Provides
    @Singleton
    fun provideOrderApi(
        @DelivaryServiceRetrofit retrofit: Retrofit
    ): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }
}