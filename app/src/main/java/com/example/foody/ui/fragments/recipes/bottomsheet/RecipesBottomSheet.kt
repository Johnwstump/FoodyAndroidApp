package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.trimmedLength
import androidx.room.util.StringUtil
import com.example.foody.R
import com.example.foody.util.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.apache.commons.lang3.StringUtils


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

        for (mealType in mealTypes) {
            // So apparently Chip doesn't have the constructor to specify a style programmatically, which is annoying, so we create
            // a custom layout which just applies the style and inflate it here
            val chip = layoutInflater.inflate(R.layout.custom_chip, chipGroup, false) as Chip
            chip.text = mealType
            chipGroup.addView(chip)

            if (StringUtils.equalsIgnoreCase(mealType, Constants.DEFAULT_MEAL_TYPE)) {
                chip.isChecked = true
            }
        }
    }

    private fun addDietTypeChips() {
        Log.d("RecipesBottomSheet", "Adding Diet Type Chips")
        val chipGroup: ChipGroup =
            requireView().findViewById(com.example.foody.R.id.dietType_chipGroup)

        val dietTypes = resources.getStringArray(com.example.foody.R.array.diet_types)

        for (dietType in dietTypes) {
            val chip = layoutInflater.inflate(R.layout.custom_chip, chipGroup, false) as Chip
            chip.text = dietType
            chipGroup.addView(chip)

            if (StringUtils.equalsIgnoreCase(dietType, Constants.DEFAULT_DIET_TYPE)) {
                chip.isChecked = true
            }
        }
    }
}