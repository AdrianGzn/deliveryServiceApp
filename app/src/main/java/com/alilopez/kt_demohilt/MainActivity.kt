package com.alilopez.kt_demohilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alilopez.kt_demohilt.core.navigation.AppNavGraph
import com.alilopez.kt_demohilt.core.navigation.routes.Screen
import com.alilopez.kt_demohilt.core.navigation.ui.AppDrawer
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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val authViewModel: AuthViewModel = hiltViewModel()

                val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val isAuthScreen = currentRoute == Screen.Login.route ||
                        currentRoute == Screen.Register.route

                val title = remember(currentRoute, isUserLoggedIn) {
                    when (currentRoute) {
                        Screen.Login.route -> "Iniciar Sesión"
                        Screen.Register.route -> "Registro"
                        Screen.Home.route -> "Inicio"
                        Screen.CustomerHome.route -> "Panel Cliente"
                        Screen.DeliveryHome.route -> "Panel Repartidor"
                        Screen.Posts.route -> "Posts"
                        Screen.Pets.route -> "Mascotas"
                        else -> "Mi App"
                    }
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        if (isUserLoggedIn && !isAuthScreen) {
                            AppDrawer(
                                drawerState = drawerState,
                                navController = navController,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    gesturesEnabled = isUserLoggedIn && !isAuthScreen
                ) {
                    Column {
                        if (isUserLoggedIn && !isAuthScreen) {
                            AppTopBar(
                                drawerState = drawerState,
                                title = title
                            )
                        }

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