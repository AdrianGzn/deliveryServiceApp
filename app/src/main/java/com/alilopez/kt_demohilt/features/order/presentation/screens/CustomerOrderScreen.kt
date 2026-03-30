package com.alilopez.kt_demohilt.features.order.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.presentation.viewmodels.CustomerOrderViewModel
import com.alipoez.kt_demohilt.features.order.presentation.states.CustomerOrderUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrderScreen(
    customerId: Int,
    viewModel: CustomerOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(customerId) {
        viewModel.loadOrders(customerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CustomerOrderUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is CustomerOrderUIState.Success -> {
                    val state = uiState as CustomerOrderUIState.Success
                    Column {
                        state.notifications.forEach { notification ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = notification.message,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.orders) { order ->
                                OrderCard(
                                    order = order,
                                    onCancelOrder = {
                                        viewModel.cancelOrder(order.id, customerId)
                                    }
                                )
                            }
                        }
                    }
                }
                is CustomerOrderUIState.Error -> {
                    val state = uiState as CustomerOrderUIState.Error
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadOrders(customerId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onCancelOrder: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${order.price}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = when (order.status) {
                        "pending" -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        "pickup" -> MaterialTheme.colorScheme.primaryContainer
                        "in_coming" -> MaterialTheme.colorScheme.secondaryContainer
                        "arrived" -> MaterialTheme.colorScheme.tertiaryContainer
                        "delivered" -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = order.statusDisplay,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                if (order.status == "pending" && order.userId != 0) {
                    Button(
                        onClick = onCancelOrder,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}