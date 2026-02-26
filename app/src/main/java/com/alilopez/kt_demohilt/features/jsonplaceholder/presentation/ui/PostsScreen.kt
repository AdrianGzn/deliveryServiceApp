package com.alilopez.kt_demohilt.features.jsonplaceholder.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.jsonplaceholder.presentation.viewmodels.PostsViewModel

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Text(state.error ?: "")
        }

        else -> {
            LazyColumn {
                items(state.posts) {
                    Text(
                        text = it.title,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
