package com.example.foodapp.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentRecipesBottomSheetBinding
import com.example.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel


    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedChip = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedChip
            mealTypeChipId = checkedId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedChip = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedChip
            dietTypeChipId = checkedId
        }

        binding.applyButton.setOnClickListener {
            recipesViewModel.saveMealAndDietType(mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId)
            val action =
                RecipesBottomSheetFragmentDirections
                    .actionRecipesBottomSheetFragmentToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("BottomSheet", e.message.toString())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}