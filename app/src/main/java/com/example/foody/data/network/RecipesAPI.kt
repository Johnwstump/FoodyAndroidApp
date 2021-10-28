package com.example.foody.data.network

import com.example.foody.models.Recipes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipesAPI {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes (
        @QueryMap queries : Map<String, String>
    ) : Response<Recipes>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes (
        @QueryMap searchQueries : Map<String, String>
    ) : Response<Recipes>
}