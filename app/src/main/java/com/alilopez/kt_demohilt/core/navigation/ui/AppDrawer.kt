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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alilopez.kt_demohilt.core.navigation.routes.Screen
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

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
                label = { Text("Inicio") },
                selected = currentRoute == Screen.Home.route,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            )

            NavigationDrawerItem(
                label = { Text("Posts") },
                selected = currentRoute == Screen.Posts.route,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Screen.Posts.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            )

            NavigationDrawerItem(
                label = { Text("Mascotas") },
                selected = currentRoute == Screen.Pets.route,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Screen.Pets.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}