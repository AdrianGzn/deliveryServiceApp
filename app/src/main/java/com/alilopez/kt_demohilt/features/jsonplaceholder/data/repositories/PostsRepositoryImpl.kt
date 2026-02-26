package com.alilopez.kt_demohilt.features.jsonplaceholder.data.repositories

import com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasources.remote.api.JsonPlaceHolderApi
import com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasourses.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.model.Post
import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: JsonPlaceHolderApi
) : PostRepository {

    override suspend fun getPosts(): List<Post> {
        return api.getPosts().map { it.toDomain() }
    }
}