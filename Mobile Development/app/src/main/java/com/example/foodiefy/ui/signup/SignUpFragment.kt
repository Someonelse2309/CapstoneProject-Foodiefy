package com.example.foodiefy.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.foodiefy.MainActivity
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentSignInBinding
import com.example.foodiefy.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val signUptosignIn = binding.signIn
        val createaccount = binding.submitButton
        val terms = binding.tnc

        // Navigasi ke SignIn
        signUptosignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        // Navigasi ke Terms and Conditions
        terms.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_termsFragment)
        }

        // Aksi tombol Submit
        createaccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_welcomeFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}