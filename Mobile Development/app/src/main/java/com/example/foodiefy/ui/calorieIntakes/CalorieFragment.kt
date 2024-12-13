package com.example.foodiefy.ui.calorieIntakes

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
import com.example.foodiefy.databinding.FragmentCalorieBinding
import com.example.foodiefy.databinding.FragmentWeightBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalorieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalorieFragment : Fragment() {
    private var _binding: FragmentCalorieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalorieBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.continueButton.setOnClickListener{
            findNavController().navigate(R.id.action_calorieFragment_to_mainActivity)
        }
        binding.icBack.setOnClickListener{
            findNavController().navigate(R.id.action_calorieFragment_to_activityFragment)
        }


        Glide.with(this)
            .load(R.drawable.ic_back)
            .circleCrop()
            .into(binding.icBack)


        Glide.with(this)
            .load(R.drawable.api)
            .into(binding.imgApi)
        return root
    }

}