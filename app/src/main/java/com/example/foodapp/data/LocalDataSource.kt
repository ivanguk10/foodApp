package com.example.foodapp.data

import com.example.foodapp.data.database.entities.RecipeEntity
import com.example.foodapp.data.database.RecipesDao
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.data.database.entities.FoodTriviaEntity
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

    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavoriteRecipe(favoriteEntity)
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

    suspend fun insertAllFavoriteRecipes(listOfFavoriteEntity: List<FavoriteEntity>) {
        recipesDao.insertAllFavoriteRecipes(listOfFavoriteEntity)
    }

    suspend fun insertFoodTrivia(foodTriviaEntity: FoodTriviaEntity) {
        recipesDao.insertFoodTrivia(foodTriviaEntity)
    }

    fun readFoodTrivia(): Flow<List<FoodTriviaEntity>> {
        return recipesDao.readFoodTrivia()
    }

}
