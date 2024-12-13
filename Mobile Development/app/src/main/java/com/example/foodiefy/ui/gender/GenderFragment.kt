@file:Suppress("UNREACHABLE_CODE")

package com.example.foodiefy.ui.gender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentGenderBinding
import com.example.foodiefy.databinding.FragmentWelcomeBinding

class GenderFragment : Fragment() {
    private var _binding: FragmentGenderBinding? = null
    private val binding get() = _binding!!
    private var selectedGender: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val backToWelcome = binding.icBack

        setupUI()

        return root
    }

    private fun setupUI() {
        binding.continueButton.isEnabled = false // Awalnya nonaktif
        binding.continueButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hijau_pucat))

        // Set klik listener untuk img_female
        binding.imgFemale.setOnClickListener {
            selectedGender = "female"
            updateOutline(binding.imgFemale, R.drawable.circle_pink_outline)
            clearOutline(binding.imgMale) // Hapus outline di img_male
            enableContinueButton()
        }

        // Set klik listener untuk img_male
        binding.imgMale.setOnClickListener {
            selectedGender = "male"
            updateOutline(binding.imgMale, R.drawable.circle_blue_outline)
            clearOutline(binding.imgFemale) // Hapus outline di img_female
            enableContinueButton()
        }

        // Listener tombol continue
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_genderFragment_to_ageFragment)
        }

        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_genderFragment_to_welcomeFragment)
        }



        // Load gambar menggunakan Glide
        Glide.with(this)
            .load(R.drawable.female)
            .circleCrop()
            .into(binding.imgFemale)

        Glide.with(this)
            .load(R.drawable.male)
            .circleCrop()
            .into(binding.imgMale)

        Glide.with(this)
            .load(R.drawable.ic_back)
            .circleCrop()
            .into(binding.icBack)

    }



    private fun updateOutline(imageView: View, outlineRes: Int) {
        imageView.background = ContextCompat.getDrawable(requireContext(), outlineRes)
    }

    private fun clearOutline(imageView: View) {
        imageView.background = null // Menghapus outline
    }

    private fun enableContinueButton() {
        binding.continueButton.isEnabled = true
        binding.continueButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hijau_cerah))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}