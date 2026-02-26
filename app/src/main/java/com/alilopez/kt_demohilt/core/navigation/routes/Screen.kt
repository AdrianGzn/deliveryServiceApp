package com.alilopez.kt_demohilt.core.navigation.routes

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Posts : Screen("posts")
    object Pets : Screen("pets")
    object CustomerHome : Screen("customer_home")
    object DeliveryHome : Screen("delivery_home")

    object PetDetail : Screen("pet_detail/{petId}") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }
}