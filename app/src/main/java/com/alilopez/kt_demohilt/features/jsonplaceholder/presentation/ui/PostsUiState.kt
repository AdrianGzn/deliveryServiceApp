package com.alilopez.kt_demohilt.features.jsonplaceholder.presentation.ui

import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.model.Post

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String? = null
)
