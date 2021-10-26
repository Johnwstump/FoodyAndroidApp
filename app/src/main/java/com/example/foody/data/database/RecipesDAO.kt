package com.example.foody.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>
}