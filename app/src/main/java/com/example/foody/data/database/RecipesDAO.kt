package com.example.foody.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.data.database.entities.JokeEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.FoodJoke
import com.example.foody.models.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity : FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    @Insert(onConflict = REPLACE)
    suspend fun insertFoodJoke(jokeEntity: JokeEntity)

    @Query("SELECT * FROM joke_table ORDER BY id ASC")
    fun readFoodJoke() : Flow<List<JokeEntity>>
}