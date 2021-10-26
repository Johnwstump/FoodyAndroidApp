package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.data.database.RecipesEntity
import com.example.foody.models.Recipes
import com.example.foody.util.NetworkResult

class RecipesBinding {
    companion object {
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(imageView : ImageView, apiResponse: NetworkResult<Recipes>?,
                                     database : List<RecipesEntity>?){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            }
            else if (apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponseForText", "readDatabaseForText", requireAll = true)
        @JvmStatic
        fun errorTextViewVisiblity(textView: TextView, apiResponse: NetworkResult<Recipes>?,
                                   database : List<RecipesEntity>?) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }
        }
    }
}