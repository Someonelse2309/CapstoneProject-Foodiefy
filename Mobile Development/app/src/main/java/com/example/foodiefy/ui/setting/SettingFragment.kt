package com.example.foodiefy.ui.setting

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentSettingBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.hide()

        val profileSetting: View = binding.gotoProfile
        profileSetting.setOnClickListener{
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }

        val aboutUsSetting: View = binding.gotoAboutUs
        aboutUsSetting.setOnClickListener{
            findNavController().navigate(R.id.action_settingFragment_to_aboutUsFragment)
        }

        val termsSetting: View = binding.gotoTC
        termsSetting.setOnClickListener{
            findNavController().navigate(R.id.action_settingFragment_to_termsFragment)
        }

        Glide.with(this)
            .load(R.drawable.profile_image)
            .circleCrop()
            .into(binding.profileImage)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}