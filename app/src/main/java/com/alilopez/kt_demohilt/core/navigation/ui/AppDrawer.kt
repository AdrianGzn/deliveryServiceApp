package com.alilopez.kt_demohilt.core.navigation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alilopez.kt_demohilt.core.navigation.routes.Screen
import com.alilopez.kt_demohilt.features.auth.presentation.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    navController: NavController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val userRole by authViewModel.userRole.collectAsState(initial = null)
    val userName by authViewModel.userName.collectAsState(initial = "")



    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Mi App",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Divider()


            NavigationDrawerItem(
                label = {
                    Text(
                        when (userRole) {
                            "customer" -> "Mis Pedidos"
                            "delivery" -> "Panel de Repartidor"
                            else -> "Inicio"
                        }
                    )
                },
                selected = currentRoute == Screen.CustomerHome.route ||
                        currentRoute == Screen.DeliveryHome.route,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        when (userRole) {
                            "customer" -> {
                                // Navegar a CustomerHome sin perder el estado
                                navController.navigate(Screen.CustomerHome.route) {
                                    launchSingleTop = true
                                    popUpTo(Screen.CustomerHome.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            "delivery" -> {
                                navController.navigate(Screen.DeliveryHome.route) {
                                    launchSingleTop = true
                                    popUpTo(Screen.DeliveryHome.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            )



        }
    }
}