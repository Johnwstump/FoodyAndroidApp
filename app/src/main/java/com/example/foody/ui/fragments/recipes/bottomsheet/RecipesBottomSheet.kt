package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import com.example.foody.R
import com.example.foody.R.color.lightGray
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class RecipesBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.foody.R.layout.recipes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMealTypeChips()
        addDietTypeChips()
    }

    private fun addMealTypeChips() {
        Log.d("RecipesBottomSheet", "Adding Meal Type Chips")
        val chipGroup: ChipGroup =
            requireView().findViewById(com.example.foody.R.id.mealType_chipGroup)

        val mealTypes = resources.getStringArray(com.example.foody.R.array.meal_types)

        var first = true
        for (mealType in mealTypes) {
            val chip = Chip(this.requireContext())
            chip.text = mealType
            chip.setChipBackgroundColorResource(R.color.mediumGray);
            chipGroup.addView(chip)

            if (first){
                chip.isSelected = true
                first = false
            }
        }
    }

    private fun addDietTypeChips(){
        Log.d("RecipesBottomSheet", "Adding Diet Type Chips")
        val chipGroup: ChipGroup =
            requireView().findViewById(com.example.foody.R.id.dietType_chipGroup)

        val dietTypes = resources.getStringArray(com.example.foody.R.array.diet_types)

        var first = true

        for (dietType in dietTypes) {
            val chip = Chip(this.requireContext())
            chip.text = dietType
            chip.setChipBackgroundColorResource(R.color.mediumGray);
            chipGroup.addView(chip)
            if (first){
                chip.isSelected = true
                first = false
            }
        }
    }
}