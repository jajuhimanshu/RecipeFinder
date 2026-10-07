package com.example.recipefinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.model.Recipe

class RecipeAdapter(
    private var recipeList: List<Recipe>,
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val expandedRecipes =
        mutableSetOf<String>()

    class RecipeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivRecipe: ImageView =
            itemView.findViewById(R.id.ivRecipe)

        val tvRecipeName: TextView =
            itemView.findViewById(R.id.tvRecipeName)

        val tvCategory: TextView =
            itemView.findViewById(R.id.tvCategory)

        val tvArea: TextView =
            itemView.findViewById(R.id.tvArea)

        val tvViewMore: TextView =
            itemView.findViewById(R.id.tvViewMore)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_recipe,
                    parent,
                    false
                )

        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {

        val recipe =
            recipeList[position]

        holder.tvRecipeName.text =
            recipe.name

        holder.tvCategory.text =
            "Category: ${recipe.category}"

        holder.tvArea.text =
            "Cuisine: ${recipe.area}"

        Glide.with(holder.itemView.context)
            .load(recipe.imageUrl)
            .placeholder(
                R.drawable.ic_launcher_background
            )
            .into(holder.ivRecipe)

        val isExpanded =
            expandedRecipes.contains(recipe.id)

        if (isExpanded) {

            holder.tvRecipeName.maxLines =
                Int.MAX_VALUE

            holder.tvCategory.maxLines =
                Int.MAX_VALUE

            holder.tvArea.maxLines =
                Int.MAX_VALUE

            holder.tvViewMore.text =
                "••• View Less"

            holder.tvViewMore.visibility =
                View.VISIBLE

        } else {

            holder.tvRecipeName.maxLines =
                2

            holder.tvCategory.maxLines =
                2

            holder.tvArea.maxLines =
                2

            holder.tvViewMore.text =
                "••• View More"

            holder.tvViewMore.visibility =
                View.GONE
        }

        holder.itemView.post {

            val contentNeedsMore =
                holder.tvRecipeName.lineCount > 2 ||
                        holder.tvCategory.lineCount > 2 ||
                        holder.tvArea.lineCount > 2

            if (!isExpanded && contentNeedsMore) {

                holder.tvViewMore.visibility =
                    View.VISIBLE

            } else if (!isExpanded) {

                holder.tvViewMore.visibility =
                    View.GONE
            }
        }

        holder.tvViewMore.setOnClickListener {

            if (expandedRecipes.contains(recipe.id)) {

                expandedRecipes.remove(
                    recipe.id
                )

            } else {

                expandedRecipes.add(
                    recipe.id
                )
            }

            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {

            onRecipeClick(recipe)
        }
    }

    override fun getItemCount(): Int {

        return recipeList.size
    }

    fun updateRecipes(
        newList: List<Recipe>
    ) {

        recipeList =
            newList

        expandedRecipes.clear()

        notifyDataSetChanged()
    }
}