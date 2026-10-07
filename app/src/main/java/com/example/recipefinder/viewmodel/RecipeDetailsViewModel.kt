package com.example.recipefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.model.RecipeDetailsUiState
import com.example.recipefinder.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeDetailsViewModel : ViewModel() {

    private val repository =
        RecipeRepository()


    private val _uiState =
        MutableLiveData<RecipeDetailsUiState>()

    val uiState: LiveData<RecipeDetailsUiState> =
        _uiState


    fun getRecipeDetails(
        mealId: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                RecipeDetailsUiState.Loading


            try {

                val response =
                    repository.getRecipeDetails(
                        mealId
                    )


                if (response.isSuccessful) {

                    val meal =
                        response.body()
                            ?.meals
                            ?.firstOrNull()


                    if (meal == null) {

                        _uiState.value =
                            RecipeDetailsUiState.Empty

                    } else {

                        _uiState.value =
                            RecipeDetailsUiState.Success(
                                meal
                            )
                    }

                } else {

                    _uiState.value =
                        RecipeDetailsUiState.Error
                }

            } catch (e: Exception) {

                _uiState.value =
                    RecipeDetailsUiState.Error
            }
        }
    }
}