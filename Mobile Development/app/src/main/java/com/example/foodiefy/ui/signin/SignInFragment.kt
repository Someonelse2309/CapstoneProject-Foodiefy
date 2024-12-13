package com.example.foodiefy.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodiefy.MainActivity
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentProfileBinding
import com.example.foodiefy.databinding.FragmentSignInBinding
import com.example.foodiefy.ui.signup.SignUpFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val signInButton = binding.loginButton
        val signInToSignUp = binding.signUp
        val emailInput = binding.emailEdit
        val passwordInput = binding.passwordEdit

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        signInButton.isEnabled = false
        signInButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.hijau_pucat)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Periksa apakah kedua input tidak kosong
                val isEmailFilled = !emailInput.text.isNullOrEmpty()
                val isPasswordFilled = !passwordInput.text.isNullOrEmpty()
                val isFormValid = isEmailFilled && isPasswordFilled

                // Atur status dan warna tombol
                signInButton.isEnabled = isFormValid
                signInButton.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    if (isFormValid) R.color.hijau_cerah else R.color.hijau_pucat
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Tambahkan TextWatcher ke kedua input
        emailInput.addTextChangedListener(textWatcher)
        passwordInput.addTextChangedListener(textWatcher)

        signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_welcomeFragment)
        }

        signInToSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.GONE

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.VISIBLE
    }
}