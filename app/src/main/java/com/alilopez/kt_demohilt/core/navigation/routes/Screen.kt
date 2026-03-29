package com.alilopez.kt_demohilt.core.navigation.routes

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object CustomerHome : Screen("customer_home")
    object DeliveryHome : Screen("delivery_home")
    object SellerHome : Screen("seller_home")
}