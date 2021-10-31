package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.models.ExtendedIngredient
import com.example.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foody.util.CustomDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*
import org.apache.commons.lang3.StringUtils

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>(){
    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient : ExtendedIngredient = ingredientsList[position]
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredient.image)  {
            crossfade(600)
            error(R.drawable.image_not_loaded)
        }
        holder.itemView.ingredient_name.text = ingredient.name
        holder.itemView.ingredient_amount.text = ingredient.amount.toString()
        holder.itemView.ingredient_unit.text = ingredient.unit
        holder.itemView.ingredient_consistency.text = StringUtils.capitalize(ingredient.consistency)
        holder.itemView.ingredient_original.text = ingredient.original
    }

    override fun getItemCount(): Int {
       return ingredientsList.size
    }

    fun setData(newIngredients : List<ExtendedIngredient>) {
        val diffUtil = CustomDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}