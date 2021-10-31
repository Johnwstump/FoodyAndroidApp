package com.example.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.Recipes
import com.example.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
) : AndroidViewModel(application) {

    /** Room Database **/
    val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes : LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)
    }

    private fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoritesEntity)
        }
    }

    private fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }
    }

    private fun deleteAllFavoriteRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }
    }

    var recipesResponse: MutableLiveData<NetworkResult<Recipes>> = MutableLiveData();
    var searchRecipesResponse: MutableLiveData<NetworkResult<Recipes>> = MutableLiveData();

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries);
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading();

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleResponse(response)

                val recipes = recipesResponse.value!!.data

                if (recipes != null) {
                    cacheRecipes(recipes)
                }
            } catch (ex: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not Found");
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.");
        }
    }

    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(queries);
    }

    private suspend fun searchRecipesSafeCall(queries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading();

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(queries)
                searchRecipesResponse.value = handleResponse(response)
            } catch (ex: Exception) {
                searchRecipesResponse.value = NetworkResult.Error("Recipes not Found");
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection.");
        }
    }

    private fun cacheRecipes(recipes: Recipes) {
        val recipesEntity = RecipesEntity(recipes)
        insertRecipes(recipesEntity)
    }

    private fun handleResponse(response: Response<Recipes>): NetworkResult<Recipes>? =
        when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout.")
            response.code() == 402 -> NetworkResult.Error("API Key Limited.")
            response.body()!!.recipes.isNullOrEmpty() -> NetworkResult.Error("Recipes not Found")
            response.isSuccessful -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message())
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
            else -> return false;
        }

    }
}