package com.example.foodapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.Result

class RecipesDiffUtil<T>(
    private val oldRecipeList: List<T>,
    private val newRecipeList: List<T>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldRecipeList.size
    }

    override fun getNewListSize(): Int {
        return newRecipeList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRecipeList[oldItemPosition] === newRecipeList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRecipeList[oldItemPosition] == newRecipeList[newItemPosition]

//        return oldRecipeList[oldItemPosition].id == newRecipeList[newItemPosition].id &&
//                oldRecipeList[oldItemPosition].cheap == newRecipeList[newItemPosition].cheap &&
//                oldRecipeList[oldItemPosition].dairyFree == newRecipeList[newItemPosition].dairyFree &&
//                oldRecipeList[oldItemPosition].glutenFree == newRecipeList[newItemPosition].glutenFree &&
//                oldRecipeList[oldItemPosition].vegan == newRecipeList[newItemPosition].vegan &&
//                oldRecipeList[oldItemPosition].vegetarian == newRecipeList[newItemPosition].vegetarian &&
//                oldRecipeList[oldItemPosition].veryHealthy == newRecipeList[newItemPosition].veryHealthy &&
//                oldRecipeList[oldItemPosition].aggregateLikes == newRecipeList[newItemPosition].aggregateLikes &&
//                oldRecipeList[oldItemPosition].extendedIngredients == newRecipeList[newItemPosition].extendedIngredients &&
//                oldRecipeList[oldItemPosition].image == newRecipeList[newItemPosition].image &&
//                oldRecipeList[oldItemPosition].readyInMinutes == newRecipeList[newItemPosition].readyInMinutes &&
//                oldRecipeList[oldItemPosition].sourceName == newRecipeList[newItemPosition].sourceName &&
//                oldRecipeList[oldItemPosition].sourceUrl == newRecipeList[newItemPosition].sourceUrl &&
//                oldRecipeList[oldItemPosition].summary == newRecipeList[newItemPosition].summary &&
//                oldRecipeList[oldItemPosition].title == newRecipeList[newItemPosition].title

    }
}