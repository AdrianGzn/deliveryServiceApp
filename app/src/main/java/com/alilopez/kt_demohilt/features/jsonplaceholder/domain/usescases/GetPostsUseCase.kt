package com.alilopez.kt_demohilt.features.jsonplaceholder.domain.usescases

import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.repository.PostRepository
import jakarta.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke() = repository.getPosts()
}
