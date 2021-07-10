package com.example.foodapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentInstructionsBinding
import com.example.foodapp.models.Result
import com.example.foodapp.util.Constants
import com.example.foodapp.util.Constants.Companion.RECIPES_RESULT_KEY

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInstructionsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)

        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        val webSiteUrl = bundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(webSiteUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}