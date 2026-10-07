package com.example.recipefinder.repository

import com.example.recipefinder.model.MealResponse
import com.example.recipefinder.network.RetrofitClient
import retrofit2.Response

class RecipeRepository {

    suspend fun searchRecipes(
        recipeName: String
    ): Response<MealResponse> {

        return RetrofitClient.apiService
            .searchRecipes(recipeName)
    }


    suspend fun getRecipeDetails(
        mealId: String
    ): Response<MealResponse> {

        return RetrofitClient.apiService
            .getRecipeDetails(mealId)
    }
}