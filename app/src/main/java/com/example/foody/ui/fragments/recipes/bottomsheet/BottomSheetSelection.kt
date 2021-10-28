package com.example.foody.ui.fragments.recipes.bottomsheet

import android.util.Log

class BottomSheetSelection() {
    var selectionChanged = false;

    var mealType: String? = null
        set(value : String?) {
            Log.d("Setting Bottom Sheet Selection", value ?: "")
            selectionChanged = value != mealType
            field = value
        }

    var dietType: String? = null
        set(value : String?) {
            Log.d("Setting Bottom Sheet Selection", value ?: "")
            selectionChanged = value != dietType
            field = value
        }
}