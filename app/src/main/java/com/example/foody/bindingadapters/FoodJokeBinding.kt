package com.example.foody.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.data.database.entities.JokeEntity
import com.example.foody.models.FoodJoke
import com.example.foody.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {
        @BindingAdapter("readJokeApiResponse", "readJokeDatabase", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisiblity(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<JokeEntity>?
        ) {
           var showCard : Boolean = apiResponse is NetworkResult.Success
                   || (apiResponse is NetworkResult.Error && !database.isNullOrEmpty());

            if (view is MaterialCardView) {
                showView(showCard, view)
            }
            if (view is ProgressBar) {
                showView(!showCard, view)
            }
        }

        private fun showView(showView: Boolean, view: View) {
            if (showView) {
                view.visibility = View.VISIBLE
            }
            else {
                view.visibility = View.INVISIBLE
            }
        }


        @BindingAdapter("apiResponseForNoConnectionError", "databaseForNoConnectionError", requireAll = false)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<JokeEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                view.visibility = View.VISIBLE
                if (view is TextView) {
                    view.text = apiResponse.message.toString()
                }
            } else if (apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success) {
                view.visibility = View.INVISIBLE
            }
        }

    }
}