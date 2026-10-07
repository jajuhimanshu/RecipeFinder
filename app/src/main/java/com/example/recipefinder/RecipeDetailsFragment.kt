package com.example.recipefinder

import android.app.backup.BackupAgent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipefinder.model.Meal
import com.example.recipefinder.model.RecipeDetailsUiState
import com.example.recipefinder.viewmodel.RecipeDetailsViewModel

class RecipeDetailsFragment : Fragment() {


    private lateinit var btnBack: Button
    private lateinit var ivRecipeDetails: ImageView
    private lateinit var tvRecipeName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvArea: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvInstructions: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: RecipeDetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_recipe_details,
            container,
            false
        )
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )


        btnBack =
            view.findViewById(R.id.btnBack)

        ivRecipeDetails =
            view.findViewById(R.id.ivRecipeDetails)

        tvRecipeName =
            view.findViewById(R.id.tvRecipeName)

        tvCategory =
            view.findViewById(R.id.tvCategory)

        tvArea =
            view.findViewById(R.id.tvArea)

        tvIngredients =
            view.findViewById(R.id.tvIngredients)

        tvInstructions =
            view.findViewById(R.id.tvInstructions)

        progressBar =
            view.findViewById(R.id.progressBarDetails)


        viewModel =
            ViewModelProvider(this)[
                RecipeDetailsViewModel::class.java
            ]


        btnBack.setOnClickListener {

            parentFragmentManager.popBackStack()
        }


        observeUiState()


        val mealId =
            arguments?.getString("mealId")


        if (mealId.isNullOrEmpty()) {

            Toast.makeText(
                requireContext(),
                "Recipe information not available",
                Toast.LENGTH_LONG
            ).show()

            return
        }


        viewModel.getRecipeDetails(
            mealId
        )
    }


    private fun observeUiState() {

        viewModel.uiState.observe(
            viewLifecycleOwner
        ) { state ->

            when (state) {

                is RecipeDetailsUiState.Loading -> {

                    progressBar.visibility =
                        View.VISIBLE
                }


                is RecipeDetailsUiState.Success -> {

                    progressBar.visibility =
                        View.GONE

                    showRecipeDetails(
                        state.meal
                    )
                }


                is RecipeDetailsUiState.Empty -> {

                    progressBar.visibility =
                        View.GONE

                    Toast.makeText(
                        requireContext(),
                        "Recipe details not found",
                        Toast.LENGTH_LONG
                    ).show()
                }


                is RecipeDetailsUiState.Error -> {

                    progressBar.visibility =
                        View.GONE

                    Toast.makeText(
                        requireContext(),
                        "Unable to load recipe details",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun showRecipeDetails(
        meal: Meal
    ) {

        tvRecipeName.text =
            meal.strMeal
                ?: "Unknown Recipe"


        tvCategory.text =
            "Category: ${
                meal.strCategory
                    ?: "Not available"
            }"


        tvArea.text =
            "Cuisine: ${
                meal.strArea
                    ?: "Not available"
            }"


        Glide.with(this)
            .load(meal.strMealThumb)
            .placeholder(
                R.drawable.ic_launcher_background
            )
            .into(ivRecipeDetails)


        tvIngredients.text =
            buildIngredients(meal)


        tvInstructions.text =
            meal.strInstructions
                ?: "Cooking instructions not available."
    }


    private fun buildIngredients(
        meal: Meal
    ): String {

        val ingredients =
            mutableListOf<String>()


        val ingredientList =
            listOf(
                Pair(meal.strIngredient1, meal.strMeasure1),
                Pair(meal.strIngredient2, meal.strMeasure2),
                Pair(meal.strIngredient3, meal.strMeasure3),
                Pair(meal.strIngredient4, meal.strMeasure4),
                Pair(meal.strIngredient5, meal.strMeasure5),
                Pair(meal.strIngredient6, meal.strMeasure6),
                Pair(meal.strIngredient7, meal.strMeasure7),
                Pair(meal.strIngredient8, meal.strMeasure8),
                Pair(meal.strIngredient9, meal.strMeasure9),
                Pair(meal.strIngredient10, meal.strMeasure10),
                Pair(meal.strIngredient11, meal.strMeasure11),
                Pair(meal.strIngredient12, meal.strMeasure12),
                Pair(meal.strIngredient13, meal.strMeasure13),
                Pair(meal.strIngredient14, meal.strMeasure14),
                Pair(meal.strIngredient15, meal.strMeasure15),
                Pair(meal.strIngredient16, meal.strMeasure16),
                Pair(meal.strIngredient17, meal.strMeasure17),
                Pair(meal.strIngredient18, meal.strMeasure18),
                Pair(meal.strIngredient19, meal.strMeasure19),
                Pair(meal.strIngredient20, meal.strMeasure20)
            )


        for (item in ingredientList) {

            val ingredient =
                item.first?.trim()

            val measure =
                item.second?.trim()


            if (!ingredient.isNullOrEmpty()) {

                if (!measure.isNullOrEmpty()) {

                    ingredients.add(
                        "• $ingredient - $measure"
                    )

                } else {

                    ingredients.add(
                        "• $ingredient"
                    )
                }
            }
        }


        if (ingredients.isEmpty()) {

            return "Ingredients not available."
        }


        return ingredients.joinToString(
            separator = "\n"
        )
    }
}