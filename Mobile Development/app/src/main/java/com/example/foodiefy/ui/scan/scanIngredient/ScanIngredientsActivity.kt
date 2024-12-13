package com.example.foodiefy.ui.scan.scanIngredient

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.foodiefy.databinding.ActivityScanIngredientsBinding
import org.tensorflow.lite.task.gms.vision.detector.Detection
import java.text.NumberFormat
import java.util.concurrent.Executors

class ScanIngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanIngredientsBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var ingredientsDetectorHelper: IngredientsDetectorHelper
    private var isPopupShown = false
    private var scanStartTime: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val resolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()
            val imageAnalyzer = ImageAnalysis.Builder().setResolutionSelector(resolutionSelector)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
            imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
                ingredientsDetectorHelper.detectObject(image)
            }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@ScanIngredientsActivity, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))

        ingredientsDetectorHelper = IngredientsDetectorHelper(
            context = this,
            detectorListener = object : IngredientsDetectorHelper.DetectorListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ScanIngredientsActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(
                    results: MutableList<Detection>?,
                    inferenceTime: Long,
                    imageHeight: Int,
                    imageWidth: Int
                ) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {

                                if (scanStartTime == null) {
                                    scanStartTime = System.currentTimeMillis()
                                }

                                binding.overlay.setResults(
                                    results, imageHeight, imageWidth
                                )

                                val builder = StringBuilder()
                                for (result in results) {
                                    val displayResult =
                                        "${result.categories[0].label} " + NumberFormat.getPercentInstance()
                                            .format(result.categories[0].score).trim()
                                    builder.append("$displayResult \n")
                                }

                                binding.tvResult.text = builder.toString()

                                Handler().postDelayed({
                                    // Menampilkan pop-up hanya setelah 3 detik
                                    showScanResultPopup(results)
                                    stopCamera()  // Berhenti setelah pop-up ditampilkan
                                }, 3000)  // 3 detik
                            } else {
                                binding.overlay.clear()
                                binding.tvResult.text = ""
                            }
                        }

                        // Force a redraw
                        binding.overlay.invalidate()
                    }
                }
            }
        )

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun showScanResultPopup(results: MutableList<Detection>) {
        // Menyusun hasil deteksi dalam format string
        val stringBuilder = StringBuilder()
        for (result in results) {
            val displayResult = "${result.categories[0].label} " +
                    NumberFormat.getPercentInstance().format(result.categories[0].score).trim()
            stringBuilder.append("$displayResult\n")
        }

        val newResult = stringBuilder.toString()

        if (!isPopupShown) {
            // Tampilkan pop-up hanya jika belum ditampilkan
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Your Ingredients :")
            builder.setMessage(newResult)

            builder.setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
                isPopupShown = false
                startCamera()
            }

            val dialog = builder.create()
            dialog.show()

            // Set flag untuk menandakan bahwa pop-up telah ditampilkan
            isPopupShown = true
        }
    }

    private fun stopCamera() {
        val cameraProvider = ProcessCameraProvider.getInstance(this).get()
        cameraProvider.unbindAll() // Nonaktifkan semua use-case kamera
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}