package com.alilopez.kt_demohilt.features.order.presentation.screens

import android.media.RingtoneManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.order.domain.entities.Order
import com.alilopez.kt_demohilt.features.order.presentation.status.CustomerOrderUIState
import com.alilopez.kt_demohilt.features.order.presentation.viewmodels.CustomerOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrderScreen(
    customerId: Int,
    onBack: () -> Unit,
    viewModel: CustomerOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Lógica para el sonido de notificación
    val notifications = (uiState as? CustomerOrderUIState.Success)?.notifications ?: emptyList()
    var lastNotificationCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(notifications.size) {
        if (notifications.size > lastNotificationCount) {
            try {
                val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, notificationUri)
                ringtone.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        lastNotificationCount = notifications.size
    }

    LaunchedEffect(customerId) {
        viewModel.loadOrders(customerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
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
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val pulseAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.6f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "pulseAlpha"
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Ícono con pulso animado
                                    Box(contentAlignment = Alignment.Center) {
                                        Surface(
                                            modifier = Modifier.size(36.dp),
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = pulseAlpha * 0.25f)
                                        ) {}
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // Texto de la notificación
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Actualización de pedido",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = notification.message,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    }
                                }
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