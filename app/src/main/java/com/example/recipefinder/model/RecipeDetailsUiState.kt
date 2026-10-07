package com.example.recipefinder.model

sealed class RecipeDetailsUiState {

    object Loading : RecipeDetailsUiState()

    data class Success(
        val meal: Meal
    ) : RecipeDetailsUiState()

    object Empty : RecipeDetailsUiState()

    object Error : RecipeDetailsUiState()
}