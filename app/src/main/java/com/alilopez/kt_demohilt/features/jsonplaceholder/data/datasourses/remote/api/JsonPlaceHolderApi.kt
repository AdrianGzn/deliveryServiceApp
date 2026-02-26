package com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasourses.remote.models.PostDTO
import retrofit2.http.GET
import retrofit2.http.POST

interface JsonPlaceHolderApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDTO>
}