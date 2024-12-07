package com.example.foodiefy.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodiefy.R
import com.example.foodiefy.ui.awal.AwalFragment

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delay selama 3 detik menggunakan Handler
        Handler(Looper.getMainLooper()).postDelayed({
            // Log untuk memastikan fungsi dipanggil setelah 3 detik
            Log.d("SplashScreen", "3 detik selesai, mencoba mengganti fragment...")

            // Sembunyikan splash layout
            val splashLayout = findViewById<View>(R.id.splash_layout)
            splashLayout.visibility = View.GONE
            Log.d("SplashScreen", "Splash layout disembunyikan.")

            // Tampilkan fragment container
            val fragmentContainer = findViewById<View>(R.id.fragment_container)
            fragmentContainer.visibility = View.VISIBLE
            Log.d("SplashScreen", "Fragment container ditampilkan.")

            // Panggil fungsi untuk mengganti fragment
            loadFragment(AwalFragment())
            Log.d("SplashScreen", "Fungsi loadFragment dipanggil.")
        }, 3000)
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
