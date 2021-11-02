package com.example.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavoriteRecipesAdapter
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.models.Recipe
import com.example.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.foody.ui.fragments.recipes.RecipesFragmentDirections

class FavoriteRecipesBinding {
    companion object {
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntities: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            if (favoritesEntities.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> view.visibility = View.VISIBLE
                    is TextView -> view.visibility = View.VISIBLE
                    is RecyclerView -> view.visibility = View.INVISIBLE
                }
            }
            else {
                when (view) {
                    is ImageView -> view.visibility = View.INVISIBLE
                    is TextView -> view.visibility = View.INVISIBLE
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoritesEntities)
                    }
                }
            }
        }
    }
}