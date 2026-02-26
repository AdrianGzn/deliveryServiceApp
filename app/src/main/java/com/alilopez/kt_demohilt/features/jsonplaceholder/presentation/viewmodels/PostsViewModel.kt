package com.alilopez.kt_demohilt.features.jsonplaceholder.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.jsonplaceholder.domain.usescases.GetPostsUseCase
import com.alilopez.kt_demohilt.features.jsonplaceholder.presentation.ui.PostsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getPostsUseCase()
            }.onSuccess {
                _uiState.value = PostsUiState(posts = it)
            }.onFailure {
                _uiState.value = PostsUiState(error = it.message)
            }
        }
    }
}
