package com.example.foodapp.bindingadapters

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.foodapp.data.database.entities.FavoriteEntity

class FavoriteRecipesBinding {

    companion object {
        @BindingAdapter("viewVisibility")
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoriteEntity: List<FavoriteEntity>?
        ) {
            if (favoriteEntity.isNullOrEmpty()) {
                view.visibility = View.VISIBLE
            }
            else {
                view.visibility = View.INVISIBLE
            }
        }

        //@BindingAdapter("navigateFromFavoriteToDetails")
        //@JvmStatic
        //fun navigateFromFavoriteToDetails(view: ConstraintLayout, favoriteEntity: FavoriteEntity) {
        //    view.setOnClickListener {
        //        view.findNavController()
        //    }

        //}
    }
}