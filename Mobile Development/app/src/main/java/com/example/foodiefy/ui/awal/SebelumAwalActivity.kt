package com.example.foodiefy.ui.awal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.foodiefy.R
import com.example.foodiefy.databinding.ActivityMainBinding
import com.example.foodiefy.databinding.ActivitySebelumAwalBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SebelumAwalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sebelum_awal)

        val navController = findNavController(R.id.nav_host_fragment_sebelum_awal)
        navController.navigate(R.id.awalFragment)
    }
}