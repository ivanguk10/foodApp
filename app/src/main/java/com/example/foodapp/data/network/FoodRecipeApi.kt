package com.example.foodapp.data.network

import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.FoodTrivia
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/food/trivia/random")
    suspend fun getTrivia(
        @Query("apiKey") apiKey: String
    ): Response<FoodTrivia>

}