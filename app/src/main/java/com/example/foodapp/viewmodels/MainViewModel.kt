package com.example.foodapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foodapp.data.Repository
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.data.database.entities.FoodTriviaEntity
import com.example.foodapp.data.database.entities.RecipeEntity
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.FoodTrivia
import com.example.foodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /** RoomDatabase*/

    val readRecipes: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoriteEntity>> = repository.local.readFavoriteRecipes().asLiveData()
    val readFoodTrivia: LiveData<List<FoodTriviaEntity>> = repository.local.readFoodTrivia().asLiveData()

    private fun insertRecipes(recipeEntity: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipeEntity)
        }
    }

    fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoriteEntity)
        }
    }

    fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteEntity)
        }
    }

    fun deleteAllFavoriteRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }
    }

    fun insertAllFavoriteRecipes(listOfFavoriteEntity: List<FavoriteEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertAllFavoriteRecipes(listOfFavoriteEntity)
        }
    }

    private fun insertFoodTrivia(foodTriviaEntity: FoodTriviaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodTrivia(foodTriviaEntity)
        }
    }


    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var triviaResponse: MutableLiveData<NetworkResult<FoodTrivia>> = MutableLiveData()

    fun getRecipe(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun getSearchedRecipe(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getTrivia(apiKey: String) = viewModelScope.launch {
        getTriviaSafeCall(apiKey)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getTriviaSafeCall(apiKey: String) {
        triviaResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTrivia(apiKey)
                triviaResponse.value = handleFoodTriviaResponse(response)

                val foodTrivia = triviaResponse.value!!.data
                if (foodTrivia != null) {
                    offlineCacheFoodTrivia(foodTrivia)
                }
            }
            catch (e: Exception) {
                triviaResponse.value = NetworkResult.Error("Food Trivia not found")
            }
        }
        else {
            triviaResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipeEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodTrivia(foodTrivia: FoodTrivia) {
        val foodTriviaEntity = FoodTriviaEntity(foodTrivia)
        insertFoodTrivia(foodTriviaEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleFoodTriviaResponse(response: Response<FoodTrivia>): NetworkResult<FoodTrivia> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodTrivia = response.body()
                NetworkResult.Success(foodTrivia!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}