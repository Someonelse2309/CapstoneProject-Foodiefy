package com.example.foodiefy.ui.scan.scanFood

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.ActivityDetailFoodBinding

class DetailFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFoodBinding
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data hasil klasifikasi dari Intent
        val classificationResult = intent.getStringExtra("classificationResult")
        val inferenceTime = intent.getLongExtra("inferenceTime", 0)

        // Tampilkan hasil klasifikasi dan waktu inferensi
        binding.foodName.text = classificationResult

        val imageUri = intent.getStringExtra("imageUri")

        if (imageUri != null) {
            Glide.with(this)
                .load(Uri.parse(imageUri))
                .error(R.drawable.ic_error)
                .into(binding.resultFoodImage) // Ganti dengan ID ImageView Anda
        } else {
            Log.e("DetailFoodActivity", "Image URI is null!")
        }

//        checkStoragePermissions()
    }

//    private fun checkStoragePermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                REQUEST_CODE)
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Permissions granted!")
                // Perform the operation that required the permissions
            } else {
                // Permissions were denied
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show rationale and ask again
                    showPermissionRationale()
                } else {
                    // User denied permission permanently. Guide them to app settings.
                    Toast.makeText(
                        this,
                        "Permissions are required to access files. Please enable them in settings.",
                        Toast.LENGTH_LONG
                    ).show()
                    openAppSettings()
                }
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs storage permissions to access files.")
            .setPositiveButton("Grant") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

//    private fun displayNutritionalInfo() {
//        // Assuming you fetched the recognized food and its nutritional information
//        val foodName = "Fried Rice"
//        val calories = 50
//        val carbs = 33
//        val protein = 4
//        val fats = 10
//
//        // Update UI with the food info
//        binding.foodName.text = foodName
////        binding.calories.text = "Calories: $calories"
////        binding.carbs.text = "Carbs: $carbs g"
////        binding.protein.text = "Protein: $protein g"
////        binding.fats.text = "Fats: $fats g"
//    }
}