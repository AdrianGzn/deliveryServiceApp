package com.alilopez.kt_demohilt.features.jsonplaceholder.data.di

import com.alilopez.kt_demohilt.features.jsonplaceholder.data.repositories.PostRepositoryImpl
import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.repository.PostRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostsRepository(
        postsRepositoryImpl: PostRepositoryImpl
    ): PostRepository
}