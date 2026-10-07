package com.example.recipefinder.network

import com.example.recipefinder.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php")
    suspend fun searchRecipes(
        @Query("s") recipeName: String
    ): Response<MealResponse>


    @GET("lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") mealId: String
    ): Response<MealResponse>
}