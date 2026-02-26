package com.alilopez.kt_demohilt.features.pets.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.alilopez.kt_demohilt.features.pets.presentation.ui.components.PetItem
import com.alilopez.kt_demohilt.features.pets.presentation.viewmodels.PetsViewModel

@Composable
fun PetsScreen(
    navController: NavController,
    viewModel: PetsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Mis Mascotas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )


        Spacer(modifier = Modifier.height(8.dp))


        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadPets() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            state.pets.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay mascotas registradas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.pets) { pet ->
                        PetItem(
                            pet = pet,
                            onClick = {
                                // Navegar al detalle de la mascota
                                navController.navigate("pet_detail/${pet.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}