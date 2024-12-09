package com.example.foodiefy.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val root: View = binding.root

        val signUpToSignIn = binding.signIn

        signUpToSignIn.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}