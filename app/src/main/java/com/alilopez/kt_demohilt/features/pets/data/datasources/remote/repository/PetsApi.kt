package com.alilopez.kt_demohilt.features.pets.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.pets.data.datasources.remote.models.PetDTO
import retrofit2.http.*

interface PetsApi {

    @GET("pet/getPets")
    suspend fun getPets(): List<PetDTO>

    @GET("pet/getPetById/{id}")
    suspend fun getPetById(@Path("id") id: Int): PetDTO

    @POST("pet/createPet")
    suspend fun createPet(@Body pet: PetDTO): PetDTO

    @PUT("pet/updatePet/{id}")
    suspend fun updatePet(@Path("id") id: Int, @Body pet: PetDTO): PetDTO

    @DELETE("pet/deletePet/{id}")
    suspend fun deletePet(@Path("id") id: Int)
}