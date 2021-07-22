package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.R
import com.example.foodapp.databinding.IngredientsRowLayoutBinding
import com.example.foodapp.models.ExtendedIngredient
import com.example.foodapp.util.Constants.Companion.BASE_IMAGE_URL

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL + ingredients[position].image) {
            crossfade(600)
            error(R.drawable.ic_twotone_image_24)
        }
        holder.binding.ingredientsName.text = ingredients[position].name?.uppercase()
        holder.binding.ingredientAmount.text = ingredients[position].amount.toString()
        holder.binding.ingredientUnit.text = ingredients[position].unit
        holder.binding.ingredientConsistency.text = ingredients[position].consistency
        holder.binding.ingredientOriginal.text = ingredients[position].original

    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newData: List<ExtendedIngredient>) {
        val ingredientDiffUtil = RecipesDiffUtil(ingredients, newData)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredients = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }


}