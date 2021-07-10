package com.example.foodapp.data.database

import androidx.room.TypeConverter
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodRecipeConverter {

    private var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun foodRecipeFromString(foodRecipeString: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(foodRecipeString, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun resultFromString(result: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(result, listType)
    }
}