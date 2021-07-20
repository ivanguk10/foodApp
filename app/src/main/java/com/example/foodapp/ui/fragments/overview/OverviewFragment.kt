package com.example.foodapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentOverviewBinding
import com.example.foodapp.models.Result
import com.example.foodapp.util.Constants.Companion.RECIPES_RESULT_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)

        binding.mainImageView.load(bundle?.image)
        binding.titleTextView.text = bundle?.title
        binding.timeTextView.text = bundle?.readyInMinutes.toString()
        binding.likesTextView.text = bundle?.aggregateLikes.toString()
        bundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.summaryTextView.text = summary
        }

        if (bundle?.vegetarian == true) {
            binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if (bundle?.vegan == true) {
            binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if (bundle?.dairyFree == true) {
            binding.dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if (bundle?.glutenFree == true) {
            binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if (bundle?.veryHealthy == true) {
            binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if (bundle?.cheap == true) {
            binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}