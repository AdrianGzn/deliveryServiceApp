package com.alilopez.kt_demohilt.features.food.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alilopez.kt_demohilt.features.food.domain.entities.Food
import com.alilopez.kt_demohilt.features.food.presentation.states.MarketplaceUIState
import com.alilopez.kt_demohilt.features.food.presentation.viewmodels.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    userId: Int,
    onNavigateToMyOrders: () -> Unit,
    viewModel: MarketplaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    // Manejo de mensajes (éxito/error)
    val state = uiState
    if (state is MarketplaceUIState.Success && state.message != null) {
        LaunchedEffect(state.message) {
            snackbarHostState.showSnackbar(state.message)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToMyOrders,
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                text = { Text("Mis Pedidos") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Marketplace", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { viewModel.loadProducts() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }

            when (state) {
                is MarketplaceUIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MarketplaceUIState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.message, color = Color.Red)
                            Button(onClick = { viewModel.loadProducts() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is MarketplaceUIState.Success -> {
                    if (state.products.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay productos disponibles por ahora")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.products) { product ->
                                ProductCard(
                                    product = product,
                                    onBuy = { viewModel.buyProduct(userId, product.sellerId, product.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Food,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Vendedor ID: ${product.sellerId}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
            Button(onClick = onBuy) {
                Text("Pedir")
            }
        }
    }
}