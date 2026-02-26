package com.alilopez.kt_demohilt.features.feature01.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState  // <-- Esta es la importación que falta
import androidx.compose.runtime.getValue      // <-- Esta también es necesaria para la delegación
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.auth.presentation.AuthViewModel

@Composable
fun HomeScreen(
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToDelivery: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val userRole by authViewModel.userRole.collectAsState()  // Ahora funciona

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla de Inicio")

        Spacer(modifier = Modifier.height(16.dp))

        when (userRole) {
            "customer" -> {
                Text(text = "Bienvenido Cliente")
                Button(
                    onClick = { /* Acciones de cliente */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver mis pedidos")
                }
            }
            "delivery" -> {
                Text(text = "Bienvenido Repartidor")
                Button(
                    onClick = { /* Acciones de repartidor */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver entregas disponibles")
                }
            }
            else -> {
                Text(text = "Selecciona tu rol")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToCustomer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a vista Cliente")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onNavigateToDelivery,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a vista Repartidor")
                }
            }
        }
    }
}