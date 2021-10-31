package com.example.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.models.Recipes
import com.example.foody.util.Constants
@Entity(tableName = Constants.RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe : Recipes
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0;
}