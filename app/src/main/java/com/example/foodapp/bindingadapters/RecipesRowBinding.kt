package com.example.foodapp.bindingadapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodapp.R
import com.example.foodapp.models.Result
import com.example.foodapp.ui.fragments.recipes.RecipesFragmentDirections

class RecipesRowBinding {

    companion object {

        @BindingAdapter("android:navigateToDetails")
        @JvmStatic
        fun navigateToDetails(constraintLayout: ConstraintLayout, result: Result) {
            constraintLayout.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    constraintLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("OnRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("android:loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String) {
            imageView.load(url) {
                crossfade(600)
                error(R.drawable.ic_twotone_image_24)
            }
        }

        @BindingAdapter("android:likes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("android:minutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("android:vegan")
        @JvmStatic
        fun isVeganImage(view: View, isVegan: Boolean) {
            if (isVegan) {
                when(view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }
    }
}