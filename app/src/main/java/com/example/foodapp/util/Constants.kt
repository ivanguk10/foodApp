package com.example.foodapp.util

class Constants {

    companion object {

        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "Api key"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val RECIPES_RESULT_KEY = "recipesBundle"

//        Api queries
        const val SEARCH_QUERY = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

//        Room database
        const val TABLE_NAME = "recipe_table"
        const val DATABASE_NAME = "recipes_database"

        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"

//
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "ketogenic"
        const val DEFAULT_NUMBER = "5"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCES_NAME = "foodApp_preferences"
        const val PREFERENCES_BACK_ONLINE = "backOnline"


    }
}