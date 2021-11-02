package com.example.foody.adapters

import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foody.models.Recipe
import com.example.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.foody.util.CustomDiffUtil
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {
    private var favoriteRecipes = emptyList<FavoritesEntity>();
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var holders = arrayListOf<MyViewHolder>()

    private lateinit var actionMode: ActionMode;
    private lateinit var view : View;
    private var multiSelection = false;

    class MyViewHolder(private val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEntity: FavoritesEntity) {
            binding.favoritesEntity = favoriteEntity;
            binding.executePendingBindings();
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return FavoriteRecipesAdapter.MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedFavorite = favoriteRecipes[position]
        holder.bind(selectedFavorite)
        view = holder.itemView.rootView
        holder.itemView.favoriteRecipes_rowLayout.setOnClickListener {
            Log.d("FavoriteRecipesAdapter", "Short press")

            if (multiSelection) {
                Log.d("FavoriteRecipesAdapter", "Short press with multi")

                applySelection(holder, selectedFavorite)
                return@setOnClickListener
            }

            val action =
                FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                    selectedFavorite.recipe
                )
            holder.itemView.findNavController().navigate(action)
        }

        // Long Click Listener
        holder.itemView.favoriteRecipes_rowLayout.setOnLongClickListener {
            Log.d("FavoriteRecipesAdapter", "Long press")
            if (!multiSelection) {
                Log.d("FavoriteRecipesAdapter", "Long press without multi")

                multiSelection = true
                requireActivity.startActionMode(this)
                true
            }

            false
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            unselectFavoriteRecipe(holder, currentRecipe)
        } else {
            selectFavoriteRecipe(holder, currentRecipe)
        }
    }

    private fun selectFavoriteRecipe(
        holder: FavoriteRecipesAdapter.MyViewHolder,
        currentRecipe: FavoritesEntity
    ) {
        selectedRecipes.add(currentRecipe)
        styleCardForSelection(holder)
        holders.add(holder)
        applyActionModeTitle()
    }

    private fun unselectFavoriteRecipe(
        holder: FavoriteRecipesAdapter.MyViewHolder,
        currentRecipe: FavoritesEntity
    ) {
        selectedRecipes.remove(currentRecipe)
        styleCardNormal(holder)
        holders.remove(holder)

        if (selectedRecipes.size == 0) {
            actionMode.finish()
        }
        applyActionModeTitle()
    }

    private fun styleCardNormal(holder: MyViewHolder) {
        changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
    }

    private fun styleCardForSelection(holder: MyViewHolder) {
        changeRecipeStyle(holder, R.color.selectedCardBackgroundColor, R.color.colorPrimary)
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteRecipes_rowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            1 -> actionMode.title = "1 item selected"
            else -> actionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        this.actionMode = actionMode!!
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_recipe_menu) {
            showSnackBar("${selectedRecipes.size} favorites deleted.")

            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            holders.clear()
            selectedRecipes.clear()
            actionMode?.finish()
        }

        return true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        multiSelection = false
        selectedRecipes.clear()
        holders.forEach {
            styleCardNormal(it)
        }
        holders.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val recipesDiffUtil = CustomDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun clearContextualActionMode(){
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }

}