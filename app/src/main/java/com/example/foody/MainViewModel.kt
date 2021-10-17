package com.example.foody

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.data.Repository
import com.example.foody.models.Recipes
import com.example.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<Recipes>> = MutableLiveData();

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries);
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading();

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleRecipesResponse(response)
            } catch (ex: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not Found");
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.");
        }
    }

    private fun handleRecipesResponse(response: Response<Recipes>): NetworkResult<Recipes>? =
        when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout.")
            response.code() == 402 -> NetworkResult.Error("API Key Limited.")
            response.body()!!.results.isNullOrEmpty() -> NetworkResult.Error("Recipes not Found")
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