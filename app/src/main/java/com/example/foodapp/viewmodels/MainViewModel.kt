package com.example.foodapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foodapp.data.Repository
import com.example.foodapp.data.database.RecipeEntity
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /** RoomDatabase*/

    val readRecipes: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipeEntity: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipeEntity)
        }
    }


    /** Retrofit*/
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getSearchedRecipe(searchQueries: Map<String, String>) = viewModelScope.launch {
        getSearchedRecipeSafeCall(searchQueries)
    }

    private suspend fun getSearchedRecipeSafeCall(searchQueries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(searchQueries)
                recipesResponse.value = handleFoodRecipeResponse(response)
            }
            catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    fun getRecipe(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipeResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null)
                    offlineCaching(foodRecipe)
            }
            catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }

        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCaching(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }


    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activityNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}