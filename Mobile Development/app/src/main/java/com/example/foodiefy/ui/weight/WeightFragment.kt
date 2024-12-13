package com.example.foodiefy.ui.weight

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentGenderBinding
import com.example.foodiefy.databinding.FragmentWeightBinding


class WeightFragment : Fragment() {
    private var _binding: FragmentWeightBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val weightToHeight = binding.continueButton
        val backToGender = binding.icBack

        binding.continueButton.isEnabled = false
        binding.continueButton.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.hijau_pucat)
        )

        binding.weightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isNotEmpty = !s.isNullOrEmpty()

                // Enable or disable the button based on the input
                binding.continueButton.isEnabled = isNotEmpty
                val buttonColor = if (isNotEmpty) {
                    R.color.hijau_cerah
                } else {
                    R.color.hijau_pucat
                }
                binding.continueButton.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), buttonColor)
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        backToGender.setOnClickListener{
            findNavController().navigate(R.id.action_weightFragment_to_ageFragment)
        }
        weightToHeight.setOnClickListener{
            findNavController().navigate(R.id.action_weightFragment_to_heightFragment)
        }


        Glide.with(this)
            .load(R.drawable.ic_back)
            .circleCrop()
            .into(binding.icBack)


        Glide.with(this)
            .load(R.drawable.weight)
            .into(binding.imgWeight)
        return root
    }


}