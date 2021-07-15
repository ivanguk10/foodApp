package com.example.foodapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.foodapp.R
import com.example.foodapp.adapters.PagerAdapter
import com.example.foodapp.data.database.entities.FavoriteEntity
import com.example.foodapp.databinding.ActivityDetailsBinding
import com.example.foodapp.ui.fragments.ingredients.IngredientsFragment
import com.example.foodapp.ui.fragments.instructions.InstructionsFragment
import com.example.foodapp.ui.fragments.overview.OverviewFragment
import com.example.foodapp.util.Constants.Companion.RECIPES_RESULT_KEY
import com.example.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var recipeSaved = false
    private var savedRecipeId = 0

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_details)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_recipe_menu, menu)
        val menuItem = menu?.findItem(R.id.favorite)
        checkSavedRecipes(menuItem!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        else if(item.itemId == R.id.favorite && !recipeSaved) {
            addToFavorite(item)
        }
        else if(item.itemId == R.id.favorite && recipeSaved) {
            removeFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }
                    else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            }
            catch (e: Exception) {
                Log.d("Favorite", e.message.toString())
            }
        })
    }

    private fun addToFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(
            0,
            args.result
        )
        mainViewModel.insertFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("saved to Favorites!")
        recipeSaved = true
    }

    private fun removeFromFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("removed from favorites")
        recipeSaved = false

    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            "${args.result.title} $message",
            Snackbar.LENGTH_LONG
        )
            .setAction("Okay"){}
            .show()
    }
}