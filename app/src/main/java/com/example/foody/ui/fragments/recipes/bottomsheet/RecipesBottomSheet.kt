package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.foody.R
import com.example.foody.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.apache.commons.lang3.StringUtils


class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.foody.R.layout.recipes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recipesViewModel.readMealAndDietType.asLiveData()
            .observe(viewLifecycleOwner, { mealAndDiet ->
                Log.d("RecipesBottomSheet", "Retrieved preferences")
                selectMealChip(mealAndDiet.selectedMealType)
                selectDietChip(mealAndDiet.selectedDietType)
            })

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
        }

        chipGroup.setOnCheckedChangeListener { group, selectedChip ->
            val chip = group.findViewById<Chip>(selectedChip)
            val selectedMealType = chip.text.toString().lowercase()
            recipesViewModel.saveMealType(selectedMealType)
        }
    }

    private fun selectDietChip(selectedDiet: String) {
        selectChip(selectedDiet, com.example.foody.R.id.dietType_chipGroup)
    }

    private fun selectMealChip(selectedMeal: String) {
        selectChip(selectedMeal, com.example.foody.R.id.mealType_chipGroup)
    }

    private fun selectChip(targetItem: String, chipGroupId: Int) {
        val chipGroup: ChipGroup =
            requireView().findViewById(chipGroupId)

        for (child in chipGroup.children) {
            var chip = child as Chip

            // Really this should be comparing some other value we're storing with or mapping onto
            // the text so we can handle a change in display text (or language) without invalidating
            // the preferences, but instead I'll just write this comment acknowledging my laziness
            if (StringUtils.equalsIgnoreCase(chip.text.toString(), targetItem)) {
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
        }
        chipGroup.setOnCheckedChangeListener { group, selectedChip ->
            val chip = group.findViewById<Chip>(selectedChip)
            val selectedDietType = chip.text.toString().lowercase()
            recipesViewModel.saveDietType(selectedDietType)
        }
    }
}