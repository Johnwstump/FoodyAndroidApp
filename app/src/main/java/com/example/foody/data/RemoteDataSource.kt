package com.example.foody.data

import com.example.foody.data.network.RecipesAPI
import com.example.foody.models.Recipes
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipesAPI: RecipesAPI
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<Recipes> {
        return recipesAPI.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery : Map<String, String>): Response<Recipes> {
        return recipesAPI.searchRecipes(searchQuery)
    }
}