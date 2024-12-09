package com.example.foodiefy.ui.scan.scanFood

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.foodiefy.databinding.ActivityScanFoodBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import com.example.foodiefy.R
import com.example.foodiefy.databinding.ActivityMainBinding
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ScanFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanFoodBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var isResultHandled = false
    private val REQUEST_CODE = 100
    private lateinit var foodClassifierHelper: FoodClassifierHelper

    // Define the directory where images will be saved
    private lateinit var outputDirectory: File

    private var isImageCaptured = false // Track if an image has already been captured

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize output directory
        outputDirectory = getOutputDirectory()

        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        checkStoragePermissions()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Initialize imageCapture for taking photos
            imageCapture = ImageCapture.Builder().build()

            // ImageAnalysis
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analyzer ->
                    analyzer.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                        classifyImage(imageProxy) // Proses frame untuk klasifikasi
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@ScanFoodActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))

        foodClassifierHelper = FoodClassifierHelper(
            context = this,
            classifierListener = object : FoodClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ScanFoodActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        if (!isResultHandled) { // Cek apakah hasil sudah diproses
                            results?.let { classifications ->
                                if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {
                                    val topCategory = classifications[0].categories.maxByOrNull { category -> category.score }

                                    topCategory?.let { category ->
                                        val displayResult = "${category.label}"

                                        // Kirim hasil klasifikasi ke DetailFoodActivity
                                        val photoFile = File(
                                            outputDirectory,
                                            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                                                .format(System.currentTimeMillis()) + ".jpg"
                                        )
                                        if (photoFile.exists()) {
                                            Log.d(TAG, "File berhasil disimpan: ${photoFile.absolutePath}")
                                        } else {
                                            Log.e(TAG, "File tidak ditemukan!")
                                        }
                                        val imageUri = Uri.fromFile(photoFile)
                                        val intent = Intent(this@ScanFoodActivity, DetailFoodActivity::class.java)
                                        intent.putExtra("classificationResult", displayResult)
                                        intent.putExtra("imageUri", imageUri.toString())
                                        startActivity(intent)

                                        isResultHandled = true // Tandai bahwa hasil telah diproses
                                        stopCamera()
                                    }
                                } else {
                                    Log.e("ScanFoodActivity", "Hasil klasifikasi kosong.")
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    private fun checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE)
        }
    }

    private fun stopCamera() {
        val cameraProvider = ProcessCameraProvider.getInstance(this).get()
        cameraProvider.unbindAll() // Nonaktifkan semua use-case kamera
    }

    private fun classifyImage(imageProxy: ImageProxy) {
        foodClassifierHelper.classifyImage(imageProxy) // Kirim gambar ke FoodClassifierHelper

        imageProxy.close() // Pastikan imageProxy ditutup setelah selesai
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

//    private fun captureAndClassifyImage() {
//        val imageCapture = imageCapture ?: return
//        imageCapture.takePicture(
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageCapturedCallback() {
//                override fun onCaptureSuccess(imageProxy: ImageProxy) {
//                    // Kirim gambar untuk diklasifikasi
//                    foodClassifierHelper.classifyImage(imageProxy)
//                    imageProxy.close() // Jangan lupa menutup imageProxy
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Toast.makeText(this@ScanFoodActivity, "Gagal menangkap gambar", Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, "Error capturing image: ${exception.message}")
//                }
//            }
//        )
//    }

//    private fun captureImage() {
//        val imageCapture = imageCapture ?: return
//
//        val photoFile = File(
//            outputDirectory,
//            SimpleDateFormat(
//                FILENAME_FORMAT, Locale.US
//            ).format(System.currentTimeMillis()) + ".jpg"
//        )
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions, ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    Log.d(TAG, "Photo capture succeeded: $savedUri")
//
//                    // Pass the captured image URI to a new activity
//                    val intent = Intent(this@ScanFoodActivity, DetailFoodActivity::class.java)
//                    intent.putExtra("imageUri", savedUri.toString())
//                    startActivity(intent)
//                }
//            }
//        )
//    }

    // Get the directory where files will be saved
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
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
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}