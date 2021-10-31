package com.example.foody.ui.fragments.recipedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.IngredientsAdapter
import com.example.foody.models.Recipe
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val ingredientsAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val arguments = requireArguments()
        val result = requireNotNull(arguments.getParcelable<Recipe>(getString(R.string.recipe_arg)))

        setupRecyclerView(view)

        result.extendedIngredients.let {
            ingredientsAdapter.setData(it)
        }

        return view
    }

    private fun setupRecyclerView(view: View){
        view.ingredients_recyclerView.adapter = ingredientsAdapter
        view.ingredients_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}