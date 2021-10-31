package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName("results")
    val recipes: List<Recipe>
)