package com.example.foody.data

import android.util.Log
import com.example.foody.data.network.RecipesAPI
import com.example.foody.models.FoodJoke
import com.example.foody.models.Recipes
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipesAPI: RecipesAPI
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<Recipes> {
        Log.d("Remoted Data Source", "Performing query with parameters:$queries")
        return recipesAPI.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery : Map<String, String>): Response<Recipes> {
        Log.d("Remoted Data Source", "Performing query with parameters:$searchQuery")
        return recipesAPI.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey : String): Response<FoodJoke> {
        Log.d("Remoted Data Source", "Retrieving Food Joke")
        return recipesAPI.getRandomJoke(apiKey)
    }
}