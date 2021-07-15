package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foodapp.models.Result
import com.example.foodapp.ui.fragments.favourites.FavoriteRecipesFragmentDirections

class FavoriteRecipesAdapter: RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {

    private var favoriteRecipes = emptyList<FavoriteEntity>()

    class MyViewHolder(val binding: FavoriteRecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favoriteEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favoriteRecipes[position]
        holder.bind(currentItem)

        holder.binding.favoriteRecipeRowLayout.setOnClickListener {
            val action = FavoriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(currentItem.result)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newData: List<FavoriteEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favoriteRecipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}