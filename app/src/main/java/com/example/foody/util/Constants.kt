package com.example.foody.util

class Constants {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "2fd86f23b47d4f8c869e1f68fd2adda5"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_SEARCH = "query"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Room Database
        const val DATABASE_NAME = "recipes_database";
        const val RECIPES_TABLE = "recipes_table";

        // Bottom Sheet Preferences
        const val PREFERENCES_NAME = "foody_preferences"

        // Really need to reference the resources here, but I don't have app context and I
        // don't love the alternatives. This is definitely smelly though.
        // TODO
        const val DEFAULT_RECIPES_COUNT = 50
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_DIET_TYPE = "dietType"
    }
}