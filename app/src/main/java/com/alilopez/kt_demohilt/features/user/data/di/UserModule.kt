package com.alilopez.kt_demohilt.features.user.data.di

import com.alilopez.kt_demohilt.features.user.data.repositories.UserRepositoryImpl
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}