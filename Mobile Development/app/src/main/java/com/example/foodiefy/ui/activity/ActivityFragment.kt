package com.example.foodiefy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Tombol Continue Button
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_activityFragment_to_calorieFragment)
        }

        // Tombol Back Button
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_activityFragment_to_heightFragment)
        }

        // Load gambar menggunakan Glide
        Glide.with(this)
            .load(R.drawable.ic_back)
            .circleCrop()
            .into(binding.icBack)

        Glide.with(this)
            .load(R.drawable.active)
            .into(binding.imgActive)

        Glide.with(this)
            .load(R.drawable.normal)
            .into(binding.imgNormal)

        Glide.with(this)
            .load(R.drawable.inactive)
            .into(binding.imgInactive)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
