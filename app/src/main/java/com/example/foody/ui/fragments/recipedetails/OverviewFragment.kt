package com.example.foody.ui.fragments.recipedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.models.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.recipes_row_layout.view.*


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = requireArguments()
        val result = requireNotNull(arguments.getParcelable<Result>(getString(R.string.result_arg)))

        view.main_imageView.load(result.image)
        view.overviewTitle_textView.text = result.title
        view.like_textView.text = result.aggregateLikes.toString()
        view.time_textView.text = result.readyInMinutes.toString()
        view.summary_textView.text = result.summary

        setHasAttribute(result.vegetarian, view.vegetarian_group);
        setHasAttribute(result.vegan, view.vegan_group);
        setHasAttribute(result.glutenFree, view.glutenFree_group);
        setHasAttribute(result.dairyFree, view.dairyFree_group);
        setHasAttribute(result.veryHealthy, view.healthy_group);
        setHasAttribute(result.cheap, view.cheap_group);
    }

    private fun setHasAttribute(hasAttribute: Boolean, vegetarianGroup: Group?) {
        if (!hasAttribute) {
            return
        }

        for (id in vegetarianGroup!!.referencedIds) {
            when (val view = requireView().findViewById<View>(id)) {
                is TextView -> view.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green
                    )
                )
                is ImageView -> view.setColorFilter(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green
                    )
                )
            }
        }
    }
}