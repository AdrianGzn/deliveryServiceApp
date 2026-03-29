package com.alilopez.kt_demohilt.features.user.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("address") val address: String? = null,
    @SerializedName("establishmentName") val establishmentName: String? = null,
    @SerializedName("establishmentAddress") val establishmentAddress: String? = null
)