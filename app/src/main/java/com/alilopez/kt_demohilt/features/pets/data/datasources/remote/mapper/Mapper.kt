package com.alilopez.kt_demohilt.features.pets.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.models.PetDTO
import com.alilopez.kt_demohilt.features.pets.domain.model.Pet

fun PetDTO.toDomain() = Pet(
    id = id,
    group = group,
    control = control,
    race = race,
    age = age,
    gender = gender,
    weight = weight,
    bodyCondition = bodyCondition,
    diagnosis = diagnosis,
    degreeLameness = degreeLameness,
    onsetTimeSymptoms = onsetTimeSymptoms,
    name = name,
    owner = owner,
    color = color,
    lastAppointment = lastAppointment,
    image = image
)

fun Pet.toDTO() = PetDTO(
    id = id,
    group = group,
    control = control,
    race = race,
    age = age,
    gender = gender,
    weight = weight,
    bodyCondition = bodyCondition,
    diagnosis = diagnosis,
    degreeLameness = degreeLameness,
    onsetTimeSymptoms = onsetTimeSymptoms,
    name = name,
    owner = owner,
    color = color,
    lastAppointment = lastAppointment,
    image = image
)