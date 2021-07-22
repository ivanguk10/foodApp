package com.example.foodapp.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodapp.data.database.entities.FoodTriviaEntity
import com.example.foodapp.models.FoodTrivia
import com.example.foodapp.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodTriviaBinding {

    companion object {

        @BindingAdapter("readApiResponseTrivia", "readDatabaseTrivia", requireAll = false)
        @JvmStatic
        fun visibleOrNotCardAndProgress(
            view: View,
            apiResponse: NetworkResult<FoodTrivia>?,
            database: List<FoodTriviaEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        @BindingAdapter("readApiResponseTriviaError", "readDatabaseTriviaError", requireAll = true)
        @JvmStatic
        fun visibleOrNotError(
            view: View,
            apiResponse: NetworkResult<FoodTrivia>?,
            database: List<FoodTriviaEntity>?
        ) {
            if(database != null){
                if(database.isEmpty()){
                    view.visibility = View.VISIBLE
                    if(view is TextView){
                        if(apiResponse != null){
                            view.text = apiResponse.message.toString()
                            Log.i("apiResponse", apiResponse.message.toString())
                        }
                        else {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }
            }
            if(apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }
        }

    }
}