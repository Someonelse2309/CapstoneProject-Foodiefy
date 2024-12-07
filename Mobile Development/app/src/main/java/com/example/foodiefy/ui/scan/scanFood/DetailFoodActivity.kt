package com.example.foodiefy.ui.scan.scanFood

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodiefy.R
import com.example.foodiefy.databinding.ActivityDetailFoodBinding

class DetailFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the image URI passed from ScanFoodActivity
        val imageUri = intent.getStringExtra("imageUri")

        // Display the captured image
        if (imageUri != null) {
            binding.resultFoodImage.setImageURI(Uri.parse(imageUri))
        }

        // You can now process the image and display food-related information
        displayNutritionalInfo()
    }

    private fun displayNutritionalInfo() {
        // Assuming you fetched the recognized food and its nutritional information
        val foodName = "Fried Rice"
        val calories = 50
        val carbs = 33
        val protein = 4
        val fats = 10

        // Update UI with the food info
        binding.foodName.text = foodName
//        binding.calories.text = "Calories: $calories"
//        binding.carbs.text = "Carbs: $carbs g"
//        binding.protein.text = "Protein: $protein g"
//        binding.fats.text = "Fats: $fats g"
    }
}