package com.alilopez.kt_demohilt.features.pets.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PetDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("group") val group: String,
    @SerializedName("control") val control: String,
    @SerializedName("race") val race: String,
    @SerializedName("age") val age: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("bodyCondition") val bodyCondition: Int,
    @SerializedName("diagnosis") val diagnosis: String,
    @SerializedName("degreeLameness") val degreeLameness: Int,
    @SerializedName("onsetTimeSymptoms") val onsetTimeSymptoms: Date,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("color") val color: String,
    @SerializedName("lastAppointment") val lastAppointment: Date,
    @SerializedName("image") val image: String
)