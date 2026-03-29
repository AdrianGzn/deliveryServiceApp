package com.alilopez.kt_demohilt.features.order.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.alipoez.kt_demohilt.features.order.presentation.states.CustomerOrderUIState
import com.alipoez.kt_demohilt.features.order.presentation.viewmodels.CustomerOrderViewModel

@Composable
fun CustomerOrderScreen(
    viewModel: CustomerOrderViewModel = hiltViewModel(),
    customerId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(customerId) {
        viewModel.loadOrders(customerId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is CustomerOrderUIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is CustomerOrderUIState.Success -> {
                CustomerOrderMarketplaceContent(
                    state = state,
                    currentCustomerId = customerId,
                    onRequestOrder = { viewModel.requestOrder(it) },
                    onCancelOrder = { viewModel.cancelOrder(it, customerId) }
                )
            }

            is CustomerOrderUIState.Error -> {
                Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CustomerOrderMarketplaceContent(
    state: CustomerOrderUIState.Success,
    currentCustomerId: Int,
    onRequestOrder: (Int) -> Unit,
    onCancelOrder: (Int) -> Unit
) {
    if (state.orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay ofertas de vendedores disponibles.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.orders) { order ->
                CustomerMarketplaceCard(
                    order = order,
                    isMyOrder = order.userId == currentCustomerId,
                    onRequest = { onRequestOrder(order.id) },
                    onCancel = { onCancelOrder(order.id) }
                )
            }
        }
    }
}

@Composable
fun CustomerMarketplaceCard(
    order: Order,
    isMyOrder: Boolean,
    onRequest: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isMyOrder) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(order.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                CustomerStatusChip(status = order.status)
            }

            Text(order.description, color = Color.Gray)
            Text("$${order.price}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            
            Text("Vendedor: ${order.establishmentName}", fontSize = 12.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(8.dp))

            if (!isMyOrder && order.userId == 0) {
                Button(onClick = onRequest, modifier = Modifier.fillMaxWidth()) {
                    Text("¡Lo quiero! (Pedir)")
                }
            } else if (isMyOrder) {
                Text("Este es tu pedido actual", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (order.status == "pending") {
                    OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancelar mi pedido")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerStatusChip(status: String) {
    val text = when(status) {
        "pending" -> "Pendiente"
        "pickup" -> "Preparado"
        "in_coming" -> "En camino"
        "delivered" -> "Entregado"
        else -> status
    }
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
    }
}
