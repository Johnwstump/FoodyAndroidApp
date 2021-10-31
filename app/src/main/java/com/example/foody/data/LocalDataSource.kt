package com.example.foody.data

import com.example.foody.data.database.RecipesDAO
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.data.database.entities.RecipesEntity
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
}