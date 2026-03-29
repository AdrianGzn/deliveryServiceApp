package com.alilopez.kt_demohilt.features.food.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.food.data.datasources.remote.model.FoodDTO
import retrofit2.http.*

interface FoodApi {
    @POST("api/food")
    suspend fun createFood(@Body food: FoodDTO): FoodDTO

    @GET("api/food")
    suspend fun getAllFood(): List<FoodDTO>

    @GET("api/food/{id}")
    suspend fun getFoodById(@Path("id") id: Int): FoodDTO

    @GET("api/food/seller/{sellerId}")
    suspend fun getFoodBySeller(@Path("sellerId") sellerId: Int): List<FoodDTO>

    @PUT("api/food/{id}")
    suspend fun updateFood(@Path("id") id: Int, @Body food: FoodDTO): FoodDTO

    @DELETE("api/food/{id}")
    suspend fun deleteFood(@Path("id") id: Int)
}