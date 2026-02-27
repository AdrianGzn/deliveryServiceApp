package com.alipoez.kt_demohilt.features.order.data.di

import com.alipoez.kt_demohilt.features.order.data.repositories.OrderRepositoryImpl
import com.alipoez.kt_demohilt.features.order.domain.repositories.OrderRepository
import com.alipoez.kt_demohilt.features.order.domain.usecase.*
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

// Separamos los Provides en otro módulo o podemos usar un companion object
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