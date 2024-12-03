package com.example.foodiefy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.show()

        Glide.with(this)
            .load(R.drawable.profile_image)
            .circleCrop()
            .into(binding.profileImage)

        Glide.with(this)
            .load(R.drawable.wajan)
            .into(binding.wajan)

        Glide.with(this)
            .load(R.drawable.kiwi)
            .into(binding.kiwi)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}