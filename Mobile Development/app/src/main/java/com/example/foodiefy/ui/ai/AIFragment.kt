package com.example.foodiefy.ui.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentAiBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AIFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val AIViewModel = ViewModelProvider(this).get(AIViewModel::class.java)
        _binding = FragmentAiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.hide()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}