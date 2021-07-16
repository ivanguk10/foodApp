package com.example.foodapp.data.database

import androidx.room.*
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.data.database.entities.RecipeEntity
import com.example.foodapp.models.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    @Insert
    suspend fun insertAllFavoriteRecipes(listOfFavoriteEntity: List<FavoriteEntity>)
}