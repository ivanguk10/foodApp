package com.example.foodapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodapp.data.database.RecipeEntity
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.util.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun showImage(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipeEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            }
            else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.INVISIBLE
            }
            else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponseTxt", "readDatabaseTxt", requireAll = true)
        @JvmStatic
        fun showText(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipeEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            }
            else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.INVISIBLE
            }
            else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponseLayout", "readDatabaseLayout", requireAll = true)
        @JvmStatic
        fun hidePlaceholder(
            linearLayout: LinearLayout,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipeEntity>?
        ) {
            if (database != null) {
                if (apiResponse is NetworkResult.Success || database.isNotEmpty()) {
                    linearLayout.visibility = View.INVISIBLE
                }
            }
        }
    }
}