package com.example.foodapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.data.database.entities.RecipeEntity

@Database(
    entities = [RecipeEntity::class, FavoriteEntity::class, ],
    version = 2,
    exportSchema = false
)

@TypeConverters(FoodRecipeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao
}