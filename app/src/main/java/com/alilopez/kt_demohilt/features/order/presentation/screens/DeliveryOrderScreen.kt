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
import com.alilopez.kt_demohilt.features.order.presentation.viewmodels.DeliveryOrderViewModel
import com.alipoez.kt_demohilt.features.order.presentation.states.DeliveryOrderUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryOrderScreen(
    deliveryId: Int,
    viewModel: DeliveryOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(deliveryId) {
        viewModel.loadData(deliveryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos - Repartidor") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is DeliveryOrderUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DeliveryOrderUIState.Success -> {
                    val state = uiState as DeliveryOrderUIState.Success
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

                        if (state.activeDelivery != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Pedido Activo",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DeliveryActiveOrderCard(
                                        order = state.activeDelivery,
                                        onUpdateStatus = { newStatus ->
                                            viewModel.updateOrderStatus(
                                                state.activeDelivery.id,
                                                newStatus,
                                                deliveryId
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Pedidos Disponibles",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.availableOrders) { order ->
                                AvailableOrderCard(
                                    order = order,
                                    onAccept = {
                                        viewModel.acceptOrder(order.id, deliveryId)
                                    }
                                )
                            }
                        }

                        if (state.myAssignedOrders.isNotEmpty()) {
                            Text(
                                text = "Mis Pedidos Asignados",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )

                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.myAssignedOrders) { order ->
                                    if (order.id != state.activeDelivery?.id) {
                                        DeliveryOrderCard(
                                            order = order,
                                            onUpdateStatus = { newStatus ->
                                                viewModel.updateOrderStatus(order.id, newStatus, deliveryId)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is DeliveryOrderUIState.Error -> {
                    val state = uiState as DeliveryOrderUIState.Error
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
                        Button(onClick = { viewModel.loadData(deliveryId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AvailableOrderCard(
    order: Order,
    onAccept: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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

            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar Pedido")
            }
        }
    }
}

@Composable
fun DeliveryOrderCard(
    order: Order,
    onUpdateStatus: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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

            Spacer(modifier = Modifier.height(12.dp))

            when (order.status) {
                "pending" -> {
                    Button(
                        onClick = { onUpdateStatus("pickup") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Marcar como Listo para Recoger")
                    }
                }
                "pickup" -> {
                    Button(
                        onClick = { onUpdateStatus("in_coming") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar Viaje")
                    }
                }
                "in_coming" -> {
                    Button(
                        onClick = { onUpdateStatus("arrived") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Llegué al Destino")
                    }
                }
                "arrived" -> {
                    Button(
                        onClick = { onUpdateStatus("delivered") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Marcar como Entregado")
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryActiveOrderCard(
    order: Order,
    onUpdateStatus: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = order.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$${order.price}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = order.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                text = "Estado: ${order.statusDisplay}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (order.status) {
            "pending" -> {
                Button(
                    onClick = { onUpdateStatus("pickup") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como Listo para Recoger")
                }
            }
            "pickup" -> {
                Button(
                    onClick = { onUpdateStatus("in_coming") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Viaje")
                }
            }
            "in_coming" -> {
                Button(
                    onClick = { onUpdateStatus("arrived") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Llegué al Destino")
                }
            }
            "arrived" -> {
                Button(
                    onClick = { onUpdateStatus("delivered") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Marcar como Entregado")
                }
            }
        }
    }
}