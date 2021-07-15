package com.example.foodapp.ui.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.FavoriteRecipesAdapter
import com.example.foodapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val favoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.favAdapter = favoriteRecipesAdapter
        setUpRecyclerView()

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner, {
            favoriteRecipesAdapter.setData(it)
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.favoritesRecyclerView.adapter = favoriteRecipesAdapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    
}