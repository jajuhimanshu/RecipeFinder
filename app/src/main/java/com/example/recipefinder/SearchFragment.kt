package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.adapter.RecipeAdapter
import com.example.recipefinder.model.Recipe
import com.example.recipefinder.model.RecipeUiState
import com.example.recipefinder.viewmodel.RecipeViewModel

class SearchFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoRecipes: TextView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeList: List<Recipe>
    private lateinit var viewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_search,
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

        etSearch =
            view.findViewById(R.id.etSearch)

        btnSearch =
            view.findViewById(R.id.btnSearch)

        recyclerView =
            view.findViewById(R.id.recyclerViewRecipes)

        progressBar =
            view.findViewById(R.id.progressBar)

        tvNoRecipes =
            view.findViewById(R.id.tvNoRecipes)

        viewModel =
            ViewModelProvider(this)[
                RecipeViewModel::class.java
            ]

        recipeList = emptyList()

        recipeAdapter =
            RecipeAdapter(recipeList) { recipe ->
                openRecipeDetails(recipe)
            }

        recyclerView.layoutManager =
            GridLayoutManager(
                requireContext(),
                2
            )

        recyclerView.adapter =
            recipeAdapter

        observeUiState()

        btnSearch.setOnClickListener {
            searchRecipes()
        }
    }

    private fun observeUiState() {

        viewModel.uiState.observe(
            viewLifecycleOwner
        ) { state ->

            when (state) {

                is RecipeUiState.Loading -> {

                    progressBar.visibility =
                        View.VISIBLE

                    tvNoRecipes.visibility =
                        View.GONE

                    recyclerView.visibility =
                        View.GONE
                }

                is RecipeUiState.Success -> {

                    progressBar.visibility =
                        View.GONE

                    tvNoRecipes.visibility =
                        View.GONE

                    recyclerView.visibility =
                        View.VISIBLE

                    recipeList =
                        state.recipes

                    recipeAdapter.updateRecipes(
                        recipeList
                    )
                }

                is RecipeUiState.Empty -> {

                    progressBar.visibility =
                        View.GONE

                    recyclerView.visibility =
                        View.GONE

                    tvNoRecipes.visibility =
                        View.VISIBLE

                    tvNoRecipes.text =
                        getString(
                            R.string.no_recipes_available
                        )

                    recipeAdapter.updateRecipes(
                        emptyList()
                    )
                }

                is RecipeUiState.Error -> {

                    progressBar.visibility =
                        View.GONE

                    recyclerView.visibility =
                        View.GONE

                    tvNoRecipes.visibility =
                        View.VISIBLE

                    tvNoRecipes.text =
                        getString(
                            R.string.recipes_unavailable
                        )

                    recipeAdapter.updateRecipes(
                        emptyList()
                    )
                }
            }
        }
    }

    private fun searchRecipes() {

        val searchText =
            etSearch.text
                .toString()
                .trim()

        if (searchText.isEmpty()) {

            Toast.makeText(
                requireContext(),
                getString(
                    R.string.enter_recipe_name
                ),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        viewModel.searchRecipes(
            searchText
        )
    }

    private fun openRecipeDetails(
        recipe: Recipe
    ) {

        val bundle =
            Bundle()

        bundle.putString(
            "mealId",
            recipe.id
        )

        val fragment =
            RecipeDetailsFragment()

        fragment.arguments =
            bundle

        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }
}