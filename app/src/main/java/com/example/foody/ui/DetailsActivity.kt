package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.models.Recipe
import com.example.foody.ui.fragments.recipedetails.IngredientsFragment
import com.example.foody.ui.fragments.recipedetails.InstructionsFragment
import com.example.foody.ui.fragments.recipedetails.OverviewFragment
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    // I very much do not love this implementation
    private var favoritesEntity : FavoritesEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val resultBundle = Bundle()
        resultBundle.putParcelable(getString(R.string.recipe_arg), args.recipe)

        val adapter = PagerAdapter(resultBundle, fragments, this)

        viewPager.adapter = adapter

        val tabTitles = ArrayList<String>()
        tabTitles.add("Overview")
        tabTitles.add("Ingredients")
        tabTitles.add("Instructions")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun checkIfRecipeIsSaved(recipe: Recipe, menu: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { response ->
            for (favoriteRecipe in response) {
                if (favoriteRecipe.recipe.id == recipe.id) {
                    Log.d(
                        "DetailsActivity",
                        "Found matching favorite: " + favoriteRecipe.recipe.id + " and " + recipe.id
                    )
                    favoritesEntity = favoriteRecipe
                    changeMenuItemColor(menu, R.color.yellow)
                    return@observe
                }
            }

            // If we didn't find a matching favorite, clear out the reference and
            // set the color to the 'not-favorited' color
            favoritesEntity = null
            changeMenuItemColor(menu, R.color.white)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkIfRecipeIsSaved(args.recipe, menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.save_to_favorites_menu) {
            toggleFavorite(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleFavorite(menu: MenuItem) {
        if (favoritesEntity != null){
            deleteFromFavorites(favoritesEntity!!, menu)
        }
        else {
            saveToFavorites(menu)
        }
    }

    private fun deleteFromFavorites(favoriteEntity: FavoritesEntity, menu: MenuItem) {
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(menu, R.color.white)
        showSnackBar("Removed from favorites.")
    }

    private fun saveToFavorites(item: MenuItem) {
        val recipe = args.recipe
        val favoritesEntity = FavoritesEntity(0, recipe)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipes saved.")
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int = R.color.yellow) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}