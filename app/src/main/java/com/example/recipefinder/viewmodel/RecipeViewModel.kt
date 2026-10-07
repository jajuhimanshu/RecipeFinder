package com.example.recipefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.model.Recipe
import com.example.recipefinder.model.RecipeUiState
import com.example.recipefinder.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val repository =
        RecipeRepository()


    private val _uiState =
        MutableLiveData<RecipeUiState>()

    val uiState: LiveData<RecipeUiState> =
        _uiState


    fun searchRecipes(
        recipeName: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                RecipeUiState.Loading


            try {

                val response =
                    repository.searchRecipes(
                        recipeName
                    )


                if (response.isSuccessful) {

                    val meals =
                        response.body()?.meals


                    if (meals.isNullOrEmpty()) {

                        _uiState.value =
                            RecipeUiState.Empty

                    } else {

                        val recipeList =
                            meals.map {

                                Recipe(
                                    it.idMeal ?: "",
                                    it.strMeal
                                        ?: "Unknown Recipe",
                                    it.strCategory
                                        ?: "Unknown Category",
                                    it.strArea
                                        ?: "Unknown Area",
                                    it.strMealThumb
                                        ?: ""
                                )
                            }


                        _uiState.value =
                            RecipeUiState.Success(
                                recipeList
                            )
                    }

                } else {

                    _uiState.value =
                        RecipeUiState.Error
                }

            } catch (e: Exception) {

                _uiState.value =
                    RecipeUiState.Error
            }
        }
    }
}