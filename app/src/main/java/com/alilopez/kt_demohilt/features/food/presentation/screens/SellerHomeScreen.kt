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
                Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
            is SellerHomeUIState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("mis productos", style = MaterialTheme.typography.headlineSmall)
                        IconButton(onClick = { viewModel.loadOrders(sellerId) }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                        }
                    }

                    if (state.orders.isEmpty()) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("No has publicado ofertas aún")
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
            Icon(Icons.Default.Add, contentDescription = "Publicar Combo")
        }
    }

    if (showAddDialog) {
        AddOrderDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, desc, price ->
                viewModel.createOrder(title, desc, price)
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
                Text(order.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                StatusBadge(order.status)
            }
            
            Text(order.description, color = Color.Gray, fontSize = 14.sp)
            Text("$${order.price}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            
            if (order.userId != 0) {
                Text("Pedido por Cliente ID: ${order.userId}", fontSize = 12.sp, color = Color.Blue)
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (order.status == "pending") {
                    Button(onClick = { onUpdateStatus("pickup") }) {
                        Text("Listo para Recoger")
                    }
                }
                if (order.status == "pickup") {
                    Button(onClick = { onUpdateStatus("delivered") }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                        Text("Marcar Entregado")
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
            "pending" -> Color.LightGray
            "pickup" -> Color.Yellow
            "delivered" -> Color.Green
            else -> Color.Cyan
        },
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(status.uppercase(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
    }
}

@Composable
fun AddOrderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Publicar Nueva Oferta") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título (ej: Combo Familiar)") })
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
                TextField(value = price, onValueChange = { price = it }, label = { Text("Precio") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, desc, price.toDoubleOrNull() ?: 0.0) },
                enabled = title.isNotBlank() && price.toDoubleOrNull() != null
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}