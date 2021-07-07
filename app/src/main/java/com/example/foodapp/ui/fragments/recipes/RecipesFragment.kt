package com.example.foodapp.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.viewmodels.MainViewModel
import com.example.foodapp.adapters.RecipesAdapter
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.util.Constants.Companion.API_KEY
import com.example.foodapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodapp.util.Constants.Companion.QUERY_API_KEY
import com.example.foodapp.util.Constants.Companion.QUERY_DIET
import com.example.foodapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodapp.util.Constants.Companion.QUERY_NUMBER
import com.example.foodapp.util.Constants.Companion.QUERY_TYPE
import com.example.foodapp.util.NetworkListener
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.util.observeOnce
import com.example.foodapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    private val recipesAdapter by lazy { RecipesAdapter() }

    private val args: RecipesFragmentArgs by navArgs()

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setUpRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, Observer { backOnline ->
            recipesViewModel.backOnline = backOnline
        })

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    //Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show()
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheetFragment)
            }
            else {
                recipesViewModel.showNetworkStatus()
            }

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_search_menu, menu)

        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(searchQuery: String?): Boolean {
        if (searchQuery != null)
            requestSearchApiData(searchQuery)
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerview
        recyclerView.adapter = recipesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()

    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    recipesAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }
                else
                    requestApiData()
            })
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.showShimmer(true)
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.hideShimmer()
    }

    private fun requestApiData() {
        mainViewModel.getRecipe(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Success -> {
                    Log.d("recipes", "ok")
                    hideShimmerEffect()
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    Log.d("recipes", "error")
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("recipes", "loading")
                    showShimmerEffect()
                }
            }
        })
    }

    private fun requestSearchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.getSearchedRecipe(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        recipesAdapter.setData(it)
                    }
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty())
                    recipesAdapter.setData(database[0].foodRecipe)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}