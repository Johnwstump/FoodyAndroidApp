package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.util.NetworkListener
import com.example.foody.util.NetworkResult
import com.example.foody.util.observeOnce
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val recipesAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setHasOptionsMenu(true)
        setupRecyclerView()
        readDatabase()

        lifecycleScope.launch {
            networkListener = NetworkListener()

            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d("NetworkListener", status.toString())
                recipesViewModel.haveInternetConnectivity = status
            }
        }

        binding.root.recipes_fab.setOnClickListener {
            if (!recipesViewModel.haveInternetConnectivity) {
                recipesViewModel.showNetworkStatus()
                return@setOnClickListener
            }

            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }

        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch() {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                Log.d("RecipesFragment", args.bottomSheetSelectionChanged.toString())

                if (database.isNotEmpty() && !args.bottomSheetSelectionChanged) {
                    Log.d("RecipesFragment", "Requesting Local Data")

                    recipesAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestAPIData()
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.shimmerRecyclerView.adapter = recipesAdapter
        binding.shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu);
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null && query.isNotEmpty()){
            searchAPIData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun requestAPIData() {
        Log.d("RecipesFragment", "Requesting Remote Data")
        showShimmerEffect()
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun searchAPIData(searchQuery : String) {
        Log.d("RecipesFragment", "Searching Remote Data")
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch() {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    recipesAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
