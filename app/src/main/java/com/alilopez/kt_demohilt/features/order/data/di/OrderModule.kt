package com.alilopez.kt_demohilt.features.order.data.di

import com.alilopez.kt_demohilt.features.order.data.repositories.OrderRepositoryImpl
import com.alilopez.kt_demohilt.features.order.domain.repositories.OrderRepository
import com.alilopez.kt_demohilt.features.order.domain.usecase.AssignDeliveryUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.CreateOrderUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.DeleteOrderUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.GetAllOrdersUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.GetOrderByIdUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.GetUserOrdersUseCase
import com.alilopez.kt_demohilt.features.order.domain.usecase.UpdateOrderStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderModule {

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository
}


@Module
@InstallIn(SingletonComponent::class)
object OrderUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateOrderUseCase(
        repository: OrderRepository
    ): CreateOrderUseCase {
        return CreateOrderUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllOrdersUseCase(
        repository: OrderRepository
    ): GetAllOrdersUseCase {
        return GetAllOrdersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserOrdersUseCase(
        repository: OrderRepository
    ): GetUserOrdersUseCase {
        return GetUserOrdersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetOrderByIdUseCase(
        repository: OrderRepository
    ): GetOrderByIdUseCase {
        return GetOrderByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateOrderStatusUseCase(
        repository: OrderRepository
    ): UpdateOrderStatusUseCase {
        return UpdateOrderStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAssignDeliveryUseCase(
        repository: OrderRepository
    ): AssignDeliveryUseCase {
        return AssignDeliveryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteOrderUseCase(
        repository: OrderRepository
    ): DeleteOrderUseCase {
        return DeleteOrderUseCase(repository)
    }
}