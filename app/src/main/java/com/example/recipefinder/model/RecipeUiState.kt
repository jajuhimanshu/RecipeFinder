
package com.example.recipefinder.model

sealed class RecipeUiState {

    object Loading : RecipeUiState()

    data class Success(
        val recipes: List<Recipe>
    ) : RecipeUiState()

    object Empty : RecipeUiState()

    object Error : RecipeUiState()
}
