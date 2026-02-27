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
import com.alilopez.kt_demohilt.features.feature01.presentation.HomeScreen
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
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route,
        modifier = modifier
    ) {
        composable(route = Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToCustomer = {
                    authViewModel.setUserLoggedIn("customer")
                    navController.navigate(Screen.CustomerHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToDelivery = {
                    authViewModel.setUserLoggedIn("delivery")
                    navController.navigate(Screen.DeliveryHome.route) {
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
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCustomer = {
                    navController.navigate(Screen.CustomerHome.route)
                },
                onNavigateToDelivery = {
                    navController.navigate(Screen.DeliveryHome.route)
                }
            )
        }

        composable(route = Screen.CustomerHome.route) {
            HomeScreen(
                onNavigateToCustomer = { /* Ya estamos en customer */ },
                onNavigateToDelivery = {
                    navController.navigate(Screen.DeliveryHome.route) {
                        popUpTo(Screen.CustomerHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.DeliveryHome.route) {
            HomeScreen(
                onNavigateToCustomer = {
                    navController.navigate(Screen.CustomerHome.route) {
                        popUpTo(Screen.DeliveryHome.route) { inclusive = true }
                    }
                },
                onNavigateToDelivery = { /* Ya estamos en delivery */ }
            )
        }

    }
}