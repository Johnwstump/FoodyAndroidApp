package com.example.foody.ui.fragments.joke

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foody.R
import com.example.foody.databinding.FragmentFoodJokeBinding
import com.example.foody.util.Constants.Companion.API_KEY
import com.example.foody.util.NetworkResult
import com.example.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var _binding : FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private val DEFAULT_FOOD_JOKE = "No Food Joke"

    private var foodJoke : String = DEFAULT_FOOD_JOKE
        set(jokeText : String) {
            binding.foodJokeTextView.text = jokeText
            field = jokeText
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false);
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    foodJoke = response.data?.text?: DEFAULT_FOOD_JOKE
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "Loading food joke")
                }
            }
        })

        binding.foodJokeTextView.movementMethod = ScrollingMovementMethod();

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, { database ->
                if (!database.isNullOrEmpty()){
                    foodJoke = database[0].foodJoke.text
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}