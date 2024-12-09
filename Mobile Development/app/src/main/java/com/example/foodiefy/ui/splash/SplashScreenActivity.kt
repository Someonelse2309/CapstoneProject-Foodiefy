package com.example.foodiefy.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodiefy.R
import com.example.foodiefy.ui.awal.AwalFragment
import com.example.foodiefy.ui.awal.SebelumAwalActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Durasi splash screen (3 detik)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SebelumAwalActivity::class.java)
            startActivity(intent)
            finish()  // Agar tidak bisa kembali ke splash screen
        }, 3000)  // Durasi dalam milidetik
    }

    // Fungsi untuk mengganti fragment
    private fun loadFragment(fragment: Fragment) {
        Log.d("SplashScreen", "Memulai transaksi fragment...")
        Log.d("SplashScreen", "Fragment yang akan dimuat: ${fragment::class.java.simpleName}")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        Log.d("SplashScreen", "Transaksi fragment selesai.")
    }
}
