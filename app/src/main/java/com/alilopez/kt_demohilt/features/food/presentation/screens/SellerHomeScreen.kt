package com.alilopez.kt_demohilt.features.food.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
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
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.food.presentation.states.SellerHomeUIState
import com.alilopez.kt_demohilt.features.food.presentation.viewmodels.SellerViewModel

@Composable
fun SellerHomeScreen(
    sellerId: Int,
    viewModel: SellerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sellerId) {
        viewModel.loadOrders(sellerId)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is SellerHomeUIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SellerHomeUIState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message, color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadOrders(sellerId) }) {
                        Text("Reintentar")
                    }
                }
            }
            is SellerHomeUIState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mis Productos", style = MaterialTheme.typography.headlineSmall)
                        IconButton(onClick = { viewModel.loadOrders(sellerId) }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                        }
                    }

                    if (state.orders.isEmpty()) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No tienes órdenes aún")
                                Text(
                                    text = "Toca el botón + para publicar un producto",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.orders) { order ->
                                SellerOrderCard(
                                    order = order,
                                    onUpdateStatus = { status ->
                                        viewModel.updateStatus(order.id, status)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Publicar Producto")
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { foodName, price ->
                viewModel.createOrder(foodName, price)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SellerOrderCard(
    order: Order,
    onUpdateStatus: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                StatusBadge(order.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = order.description,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${String.format("%.2f", order.price)}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

            if (order.userId != 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cliente ID: ${order.userId}",
                    fontSize = 12.sp,
                    color = Color.Blue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (order.status) {
                    "pending" -> {
                        Button(
                            onClick = { onUpdateStatus("pickup") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Listo para Recoger")
                        }
                    }
                    "pickup" -> {
                        Button(
                            onClick = { onUpdateStatus("delivered") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Marcar Entregado")
                        }
                    }
                    "in_coming" -> {
                        Button(
                            onClick = { onUpdateStatus("arrived") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                        ) {
                            Text("Llegó al Local")
                        }
                    }
                    "arrived" -> {
                        Button(
                            onClick = { onUpdateStatus("delivered") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Entregar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    Surface(
        color = when(status) {
            "pending" -> Color(0xFF9E9E9E)
            "pickup" -> Color(0xFFFFC107)
            "in_coming" -> Color(0xFF2196F3)
            "arrived" -> Color(0xFFFF9800)
            "delivered" -> Color(0xFF4CAF50)
            else -> Color.Gray
        },
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = when(status) {
                "pending" -> "PENDIENTE"
                "pickup" -> "LISTO"
                "in_coming" -> "EN CAMINO"
                "arrived" -> "LLEGÓ"
                "delivered" -> "ENTREGADO"
                else -> status.uppercase()
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Publicar Nuevo Producto") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nombre del producto") },
                    placeholder = { Text("Ej: Pizza Margherita") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Text("$") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull()
                    if (priceDouble != null) {
                        onConfirm(productName, priceDouble)
                    }
                },
                enabled = productName.isNotBlank() && price.toDoubleOrNull() != null
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}