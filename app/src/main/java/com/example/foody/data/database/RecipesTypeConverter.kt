package com.example.foody.data.database

import androidx.room.TypeConverter
import com.example.foody.models.Recipes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe : Recipes) : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String) : Recipes {
        val listType = object : TypeToken<Recipes>(){}.type
        return gson.fromJson(data, listType)
    }
}