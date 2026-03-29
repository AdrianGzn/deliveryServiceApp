package com.alilopez.kt_demohilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alilopez.kt_demohilt.core.navigation.AppNavGraph
import com.alilopez.kt_demohilt.core.navigation.routes.Screen
import com.alilopez.kt_demohilt.core.navigation.ui.AppTopBar
import com.alilopez.kt_demohilt.core.ui.theme.AppTheme
import com.alilopez.kt_demohilt.features.auth.presentation.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState(initial = false)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val isAuthScreen = currentRoute == Screen.Login.route ||
                        currentRoute == Screen.Register.route

                val title = remember(currentRoute, isUserLoggedIn) {
                    when (currentRoute) {
                        Screen.Login.route -> "Iniciar Sesión"
                        Screen.Register.route -> "Registro"
                        Screen.CustomerHome.route -> "Marketplace"
                        Screen.DeliveryHome.route -> "Panel Repartidor"
                        Screen.SellerHome.route -> "Mi Tienda"
                        else -> "Delivery App"
                    }
                }

                Scaffold(
                    topBar = {
                        if (isUserLoggedIn && !isAuthScreen) {
                            AppTopBar(
                                title = title,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                ) { padding ->
                    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                        AppNavGraph(
                            navController = navController,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
