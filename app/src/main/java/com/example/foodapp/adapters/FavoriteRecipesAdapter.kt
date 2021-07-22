package com.example.foodapp.adapters

import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foodapp.models.Result
import com.example.foodapp.ui.fragments.favourites.FavoriteRecipesFragmentDirections
import com.example.foodapp.viewmodels.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private lateinit var rootView: View

    private lateinit var mActionMode: ActionMode

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
        rootView = holder.itemView.rootView

        myViewHolders.add(holder)

        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

//        Single Click Listener
        holder.binding.favoriteRecipeRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections
                        .actionFavouriteRecipesFragmentToDetailsActivity(currentRecipe.result)
                holder.itemView.findNavController().navigate(action)
            }
        }
//        Long Click Listener
        holder.binding.favoriteRecipeRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            }else {
                if (selectedRecipes.size == 0) {
                    multiSelection = false
                }
                false
            }
        }

    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        holder: MyViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.binding.favoriteRecipeConstraintLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRecipeCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when(selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} recipe selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} recipes selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.delete_favorite_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if (menuItem?.itemId == R.id.delete_favorite) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBarMessage("${selectedRecipes.size} Recipe/s removed")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newData: List<FavoriteEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favoriteRecipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_LONG
        )
            .setAction("OKAY") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}