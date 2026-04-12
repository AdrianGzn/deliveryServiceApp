package com.alilopez.kt_demohilt.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alilopez.kt_demohilt.core.navigation.routes.Screen
import com.alilopez.kt_demohilt.features.auth.presentation.AuthViewModel
import com.alilopez.kt_demohilt.features.food.presentation.screens.MarketplaceScreen
import com.alilopez.kt_demohilt.features.food.presentation.screens.SellerHomeScreen
import com.alilopez.kt_demohilt.features.order.presentation.screens.CustomerOrderScreen
import com.alilopez.kt_demohilt.features.order.presentation.screens.DeliveryOrderScreen
import com.alilopez.kt_demohilt.features.user.presentation.screens.LoginScreen
import com.alilopez.kt_demohilt.features.user.presentation.screens.RegisterScreen
import com.alilopez.kt_demohilt.features.user.presentation.viewmodels.LoginViewModel
import com.alilopez.kt_demohilt.features.user.presentation.viewmodels.RegisterViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState(initial = false)
    val userRole by authViewModel.userRole.collectAsState(initial = null)
    val userId by authViewModel.userId.collectAsState(initial = null)

    val startDestination = when {
        !isUserLoggedIn -> Screen.Login.route
        userRole == "customer" -> Screen.CustomerHome.route
        userRole == "delivery" -> Screen.DeliveryHome.route
        userRole == "seller" -> Screen.SellerHome.route
        else -> Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToCustomer = { user ->
                    authViewModel.setUserLoggedIn(user)
                    navController.navigate(Screen.CustomerHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToDelivery = { user ->
                    authViewModel.setUserLoggedIn(user)
                    navController.navigate(Screen.DeliveryHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSeller = { user ->
                    authViewModel.setUserLoggedIn(user)
                    navController.navigate(Screen.SellerHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(route = Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()

            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = { user ->
                    authViewModel.setUserLoggedIn(user)
                    when (user.role) {
                        "customer" -> {
                            navController.navigate(Screen.CustomerHome.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                        "delivery" -> {
                            navController.navigate(Screen.DeliveryHome.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                        "seller" -> {
                            navController.navigate(Screen.SellerHome.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Marketplace para el Cliente
        composable(route = Screen.CustomerHome.route) {
            userId?.let { id ->
                MarketplaceScreen(
                    userId = id,
                    onNavigateToMyOrders = {
                        navController.navigate(Screen.MyOrders.route)
                    }
                )
            }
        }

        // Pantalla de pedidos del cliente
        composable(route = Screen.MyOrders.route) {
            userId?.let { id ->
                CustomerOrderScreen(customerId = id)
            }
        }

        // Pantalla principal de repartidor
        composable(route = Screen.DeliveryHome.route) {
            userId?.let { id ->
                DeliveryOrderScreen(deliveryId = id)
            }
        }

        // Pantalla principal de vendedor
        composable(route = Screen.SellerHome.route) {
            userId?.let { id ->
                SellerHomeScreen(sellerId = id)
            }
        }
    }
}
