package com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasourses.remote.mapper

import com.alilopez.kt_demohilt.features.jsonplaceholder.data.datasourses.remote.models.PostDTO
import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.model.Post

fun PostDTO.toDomain() = Post(
    id = id,
    title = title,
    body = body
)
