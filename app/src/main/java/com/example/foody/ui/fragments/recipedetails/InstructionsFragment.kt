package com.example.foody.ui.fragments.recipedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody.R
import com.example.foody.models.Recipe
import kotlinx.android.synthetic.main.fragment_instructions.view.*


class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val arguments = requireArguments()
        val result = requireNotNull(arguments.getParcelable<Recipe>(getString(R.string.recipe_arg)))

        view.instructions_webView.webViewClient = object : WebViewClient() {}

        view.instructions_webView.loadUrl(result.sourceUrl)
        return view
    }
}