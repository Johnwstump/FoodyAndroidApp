package com.example.foody.data.database

import androidx.room.TypeConverter
import com.example.foody.models.Recipe
import com.example.foody.models.Recipes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun recipesToString(recipes : Recipes) : String {
        return gson.toJson(recipes)
    }

    @TypeConverter
    fun stringToRecipes(data: String) : Recipes {
        val listType = object : TypeToken<Recipes>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun recipeToString(recipe: Recipe) : String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String) : Recipe {
        val listType = object : TypeToken<Recipe>(){}.type
        return gson.fromJson(data, listType)
    }
}