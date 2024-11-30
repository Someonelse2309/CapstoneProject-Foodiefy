package com.example.foodiefy.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentProfileBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val profileViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.GONE

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.visibility = View.GONE

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Glide.with(this)
            .load(R.drawable.profile_image)
            .circleCrop()
            .into(binding.profileImage)

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