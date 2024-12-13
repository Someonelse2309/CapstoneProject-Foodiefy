package com.example.foodiefy.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodiefy.R
import com.example.foodiefy.databinding.FragmentTermsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TermsFragment : Fragment() {
    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTermsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar?.visibility = View.GONE

        val fab = activity?.findViewById<FloatingActionButton>(R.id.scanMain)
        fab?.visibility = View.GONE

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