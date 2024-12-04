package com.example.foodiefy

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodiefy.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabFood: FloatingActionButton
    private lateinit var fabIngredients: FloatingActionButton
    private lateinit var blurOverlay: View

    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_ai, R.id.navigation_history, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fabMain = findViewById(R.id.scanMain)
        fabFood = findViewById(R.id.fabFood)
        fabIngredients = findViewById(R.id.fabIngredients)
        blurOverlay = findViewById(R.id.blurOverlay)

        fabMain.setOnClickListener {
            toggleFabMenu()
        }

        blurOverlay.setOnClickListener {
            if (isFabMenuOpen) {
                toggleFabMenu()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            fabFood.visibility = View.INVISIBLE
            fabIngredients.visibility = View.INVISIBLE
            blurOverlay.visibility = View.INVISIBLE
            isFabMenuOpen = false
        } else {
            fabFood.visibility = View.VISIBLE
            fabIngredients.visibility = View.VISIBLE
            blurOverlay.visibility = View.VISIBLE
            isFabMenuOpen = true
        }
    }
}