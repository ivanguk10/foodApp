package com.example.foodapp.ui.fragments.trivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFoodTriviaBinding
import com.example.foodapp.util.Constants.Companion.API_KEY
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodTriviaFragment : Fragment() {

    private var _binding: FragmentFoodTriviaBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private var foodTrivia = "No Food Trivia"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentFoodTriviaBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        mainViewModel.getTrivia(API_KEY)
        mainViewModel.triviaResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is NetworkResult.Success -> {
                    binding.foodTriviaTextView.text = response.data?.text
                    Log.d("foodJokeFragment", "ok")
                    foodTrivia = response.data!!.text
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    loadDataFromCache()
                    Log.d("foodJokeFragment", "error")
                }
                is NetworkResult.Loading -> {
                    Log.d("foodJokeFragment", "Loading")
                }
            }
        })


        return binding.root
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodTrivia.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && database != null) {
                    binding.foodTriviaTextView.text = database[0].foodTrivia.text
                    foodTrivia = database[0].foodTrivia.text
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodTrivia)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}