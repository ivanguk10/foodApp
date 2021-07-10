package com.example.foodapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapters.IngredientsAdapter
import com.example.foodapp.databinding.FragmentIngredientsBinding
import com.example.foodapp.models.Result
import com.example.foodapp.util.Constants.Companion.RECIPES_RESULT_KEY

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val ingredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(layoutInflater, container, false)


        setUpRecyclerView()

        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)

        bundle?.extendedIngredients?.let {
            ingredientsAdapter.setData(it)
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}