package com.alilopez.kt_demohilt.features.jsonplaceholder.domain.repository

import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
}