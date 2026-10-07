package com.example.recipefinder.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val imageUrl: String
)