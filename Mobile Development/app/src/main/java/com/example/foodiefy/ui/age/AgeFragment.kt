package com.example.foodiefy.ui.age

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
import com.example.foodiefy.databinding.FragmentAgeBinding
import com.example.foodiefy.databinding.FragmentGenderBinding


class AgeFragment : Fragment() {
    private var _binding: FragmentAgeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val genderToAge = binding.continueButton
        val backToGander = binding.icBack

        binding.continueButton.isEnabled = false
        binding.continueButton.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.hijau_pucat)
        )

        binding.ageEditText.addTextChangedListener(object : TextWatcher {
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

        genderToAge.setOnClickListener{
            findNavController().navigate(R.id.action_ageFragment_to_weightFragment)
        }


        backToGander.setOnClickListener{
            findNavController().navigate(R.id.action_ageFragment_to_genderFragment)
        }

        Glide.with(this)
            .load(R.drawable.ic_back)
            .into(binding.icBack)

        Glide.with(this)
            .load(R.drawable.age)
            .into(binding.imgAge)
        return root

    }
}