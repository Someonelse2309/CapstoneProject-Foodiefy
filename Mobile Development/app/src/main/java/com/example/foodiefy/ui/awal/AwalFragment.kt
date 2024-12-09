package com.example.foodiefy.ui.awal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodiefy.R
import com.example.foodiefy.ui.signin.SignInFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AwalFragment : Fragment(R.layout.fragment_awal) {

    private lateinit var lanjut: TextView
    private lateinit var gestureDetector: GestureDetector
    private var isAnimating = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AwalFragment", "Tampilan AwalFragment berhasil dibuat")

        // Inisialisasi TextView untuk "lanjut"
        lanjut = view.findViewById(R.id.lanjut)

        // Menambahkan GestureDetector untuk mendeteksi gerakan swipe
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?, e2: MotionEvent,
                velocityX: Float, velocityY: Float
            ): Boolean {
                if (isAnimating) {
                    Log.d("AwalFragment", "Swipe diabaikan karena animasi sedang berlangsung")
                    return false
                }

                if (e1 != null) {
                    val deltaX = e2.x - e1.x
                    val deltaY = e2.y - e1.y

                    Log.d("AwalFragment", "Fling detected: deltaX=$deltaX, deltaY=$deltaY")

                    if (Math.abs(deltaY) > Math.abs(deltaX) && deltaY < 0) {
                        if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                            Log.d("AwalFragment", "Swipe up valid, memulai animasi")
                            animateSwipeUp()
                            return true
                        } else {
                            Log.d("AwalFragment", "Swipe terlalu pendek, tidak memenuhi SWIPE_THRESHOLD")
                        }
                    } else {
                        Log.d("AwalFragment", "Gesture bukan swipe up")
                    }
                }
                return false
            }
        })

        // Menambahkan TouchListener untuk mendeteksi gerakan swipe pada TextView
        lanjut.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun animateSwipeUp() {
        Log.d("AwalFragment", "Animasi Swipe Up Dimulai")
        isAnimating = true

        val animator = ObjectAnimator.ofFloat(lanjut, "translationY", 0f, -150f)
        animator.duration = 1000
        animator.start()

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                Log.d("AwalFragment", "Animasi Swipe Up Selesai")
                isAnimating = false

                try {
                    val navController = findNavController()
                    navController.navigate(R.id.action_awalFragment_to_signInFragment)
                } catch (e: Exception) {
                    Log.e("AwalFragment", "Gagal mengganti fragment: ${e.message}", e)
                }
            }
        })
    }

    companion object {
        const val SWIPE_THRESHOLD = 150

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AwalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
