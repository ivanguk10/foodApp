package com.example.foodapp.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.FavoriteRecipesAdapter
import com.example.foodapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val favoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_delete_all_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val removedRecipes = mainViewModel.readFavoriteRecipes.value!!
        if (item.itemId == R.id.deleteAll) {
            mainViewModel.deleteAllFavoriteRecipes()

            Snackbar.make(
                binding.root,
                "All favorite recipes removed",
                Snackbar.LENGTH_LONG
            )
                .setAction("Restore") {
                    mainViewModel.insertAllFavoriteRecipes(removedRecipes)
                    favoriteRecipesAdapter.setData(removedRecipes)
                }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteRecipesAdapter.clearContextualActionMode()
    }

    
}