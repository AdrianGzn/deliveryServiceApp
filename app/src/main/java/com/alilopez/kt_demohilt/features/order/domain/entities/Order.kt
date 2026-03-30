package com.alilopez.kt_demohilt.features.order.domain.entities

data class Order(
    val id: Int = 0,
    val title: String,
    val description: String,
    val status: String,
    val price: Double,
    val userId: Int,
    val sellerId: Int,
    val deliveryId: Int? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
) {
    val isPending: Boolean get() = status == "pending"
    val isPickup: Boolean get() = status == "pickup"
    val isInComing: Boolean get() = status == "in_coming"
    val isArrived: Boolean get() = status == "arrived"
    val isDelivered: Boolean get() = status == "delivered"

    val statusDisplay: String
        get() = when (status) {
            "pending" -> "Pendiente"
            "pickup" -> "Listo para recoger"
            "in_coming" -> "En camino"
            "arrived" -> "Llegó"
            "delivered" -> "Entregado"
            else -> "Desconocido"
        }
}