package com.example.foodapp.data

import com.example.foodapp.data.database.entities.RecipeEntity
import com.example.foodapp.data.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    var recipesDao: RecipesDao
) {
    fun readRecipes(): Flow<List<RecipeEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipeEntity: RecipeEntity) {
        recipesDao.insertRecipes(recipeEntity)
    }
}