package com.alilopez.kt_demohilt.features.order.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alipoez.kt_demohilt.features.order.presentation.states.CreateOrderRequest
import com.alipoez.kt_demohilt.features.order.presentation.states.CustomerOrderUIState
import com.alipoez.kt_demohilt.features.order.presentation.viewmodels.CustomerOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrderScreen(
    viewModel: CustomerOrderViewModel = hiltViewModel(),
    customerId: Int,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateOrderDialog by remember { mutableStateOf(false) }

    LaunchedEffect(customerId) {
        viewModel.loadOrders(customerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos - Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateOrderDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Pedido")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is CustomerOrderUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CustomerOrderUIState.Success -> {
                    CustomerOrderContent(
                        state = state,
                        onCancelOrder = { viewModel.cancelOrder(it, customerId) }
                    )
                }

                is CustomerOrderUIState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.loadOrders(customerId) }
                    )
                }
            }

            (uiState as? CustomerOrderUIState.Success)?.notifications?.let {
                NotificationOverlay(
                    notifications = it.map { notification -> notification.message },
                    onDismiss = { notificationId -> viewModel.dismissNotification(notificationId) }
                )
            }


        }
    }

    if (showCreateOrderDialog) {
        CreateOrderDialog(
            onCreateOrder = { request ->
                viewModel.createOrder(
                    customerId = customerId,
                    title = request.title,
                    description = request.description,
                    establishmentName = request.establishmentName,
                    establishmentAddress = request.establishmentAddress,
                    price = request.price.toDoubleOrNull() ?: 0.0
                )
                showCreateOrderDialog = false
            },
            onDismiss = { showCreateOrderDialog = false }
        )
    }
}

@Composable
fun CustomerOrderContent(
    state: CustomerOrderUIState.Success,
    onCancelOrder: (Int) -> Unit
) {
    if (state.orders.isEmpty()) {
        EmptyOrdersContent()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.orders) { order ->
                CustomerOrderCard(
                    order = order,
                    onCancelOrder = onCancelOrder
                )
            }
        }
    }
}

@Composable
fun CustomerOrderCard(
    order: Order,
    onCancelOrder: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (order.status) {
                "delivered" -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                "cancelled" -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.title,
                    style = MaterialTheme.typography.titleLarge
                )
                StatusChip(status = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.establishmentName,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.establishmentAddress,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${order.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (order.deliveryId != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Repartidor asignado",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Buscando repartidor...",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            if (order.status == "pending" || order.status == "pickup") {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onCancelOrder(order.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancelar Pedido")
                }
            }

            if (order.status == "in_coming" || order.status == "arrived") {
                Spacer(modifier = Modifier.height(8.dp))
                TrackingInfo(order = order)
            }
        }
    }
}

@Composable
fun TrackingInfo(
    order: Order
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (order.status == "in_coming") "Repartidor en camino" else "Repartidor ha llegado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tu pedido está siendo entregado",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun EmptyOrdersContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Text(
            text = "Aún no tienes pedidos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun CreateOrderDialog(
    onCreateOrder: (CreateOrderRequest) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var establishmentName by remember { mutableStateOf("") }
    var establishmentAddress by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Nuevo Pedido") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                OutlinedTextField(value = establishmentName, onValueChange = { establishmentName = it }, label = { Text("Restaurante") })
                OutlinedTextField(value = establishmentAddress, onValueChange = { establishmentAddress = it }, label = { Text("Dirección del Restaurante") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val request = CreateOrderRequest(title, description, establishmentName, establishmentAddress, price)
                onCreateOrder(request)
            }) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun NotificationOverlay(
    notifications: List<String>,
    onDismiss: (String) -> Unit
) {
    // Implementación de la superposición de notificaciones
}
