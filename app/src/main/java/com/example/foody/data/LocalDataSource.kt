package com.example.foody.data

import com.example.foody.data.database.RecipesDAO
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.data.database.entities.JokeEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDAO: RecipesDAO
) {

    fun readRecipes() : Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDAO.insertRecipes(recipesEntity)
    }

    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>> {
        return recipesDAO.readFavoriteRecipes()
    }

    suspend fun insertFavoriteRecipe(favoriteRecipe : FavoritesEntity){
        recipesDAO.insertFavoriteRecipe(favoriteRecipe)
    }

    suspend fun deleteFavoriteRecipe(favoriteRecipe : FavoritesEntity){
        recipesDAO.deleteFavoriteRecipe(favoriteRecipe)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDAO.deleteAllFavoriteRecipes()
    }

    fun readFoodJoke() : Flow<List<JokeEntity>> {
        return recipesDAO.readFoodJoke()
    }

    suspend fun insertFoodJoke(jokeEntity: JokeEntity){
        recipesDAO.insertFoodJoke(jokeEntity)
    }
}