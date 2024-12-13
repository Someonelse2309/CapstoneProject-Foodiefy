package com.example.foodiefy.ui.height

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
import com.example.foodiefy.databinding.FragmentHeightBinding
import com.example.foodiefy.databinding.FragmentWeightBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HeightFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeightFragment : Fragment() {
    private var _binding: FragmentHeightBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeightBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val heightToActivity = binding.continueButton
        val heightToWeight = binding.icBack

        binding.continueButton.isEnabled = false
        binding.continueButton.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.hijau_pucat)
        )

        binding.heightEditText.addTextChangedListener(object : TextWatcher {
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



        heightToWeight.setOnClickListener{
            findNavController().navigate(R.id.action_heightFragment_to_weightFragment)
        }

        heightToActivity.setOnClickListener{
            findNavController().navigate(R.id.action_heightFragment_to_activityFragment)
        }


        Glide.with(this)
            .load(R.drawable.ic_back)
            .circleCrop()
            .into(binding.icBack)


        Glide.with(this)
            .load(R.drawable.height)
            .into(binding.imgHeight)
        return root
    }


}