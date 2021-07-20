package com.example.foodapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.data.database.entities.FoodTriviaEntity
import com.example.foodapp.data.database.entities.RecipeEntity
import com.example.foodapp.util.Constants.Companion.FOOD_TRIVIA_TABLE

@Database(
    entities = [RecipeEntity::class, FavoriteEntity::class, FoodTriviaEntity::class],
    version = 2,
    exportSchema = true
)

@TypeConverters(FoodRecipeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    companion object {
        val Migration1_2: Migration = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `${FOOD_TRIVIA_TABLE}` (`id` INTEGER NOT NULL, `text` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }
    }

}