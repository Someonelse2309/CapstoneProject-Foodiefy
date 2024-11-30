package com.example.foodiefy.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentAboutUsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutUsFragment : Fragment() {
    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.GONE

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.visibility = View.GONE

//        Glide.with(this)
//            .load(R.drawable.amelz)
//            .circleCrop()
//            .into(binding.ml1)
//
//        Glide.with(this)
//            .load(R.drawable.bea)
//            .circleCrop()
//            .into(binding.ml2)
//
//        Glide.with(this)
//            .load(R.drawable.andi)
//            .circleCrop()
//            .into(binding.ml3)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.VISIBLE

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.visibility = View.VISIBLE
    }
}