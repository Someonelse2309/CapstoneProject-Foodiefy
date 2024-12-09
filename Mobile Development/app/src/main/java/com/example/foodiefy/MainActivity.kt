package com.example.foodiefy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodiefy.databinding.ActivityMainBinding
import com.example.foodiefy.ui.scan.scanFood.ScanFoodActivity
import com.example.foodiefy.ui.scan.scanIngredient.ScanIngredientsActivity
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

        fabFood.setOnClickListener {
            startCameraXFood()
        }

        fabIngredients.setOnClickListener {
            startCameraXIngredients()
        }

        blurOverlay.setOnClickListener {
            if (isFabMenuOpen) {
                toggleFabMenu()
            }
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

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

    private fun startCameraXFood() {
        val intent = Intent(this, ScanFoodActivity::class.java)
        startActivity(intent)
    }

    private fun startCameraXIngredients() {
        val intent = Intent(this, ScanIngredientsActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}