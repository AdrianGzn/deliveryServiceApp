package com.alilopez.kt_demohilt.features.pets.domain.model

import java.util.Date

data class Pet(
    val id: Int,
    val group: String,
    val control: String,
    val race: String,
    val age: Int,
    val gender: String,
    val weight: String,
    val bodyCondition: Int,
    val diagnosis: String,
    val degreeLameness: Int,
    val onsetTimeSymptoms: Date,
    val name: String,
    val owner: String,
    val color: String,
    val lastAppointment: Date,
    val image: String
)