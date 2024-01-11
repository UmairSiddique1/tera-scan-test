package com.example.terascantest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terascantest.R
import com.example.terascantest.databinding.FragmentSecurityQuestionsBinding

class SecurityQuestionsFragment : Fragment() {
    private lateinit var binding:FragmentSecurityQuestionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSecurityQuestionsBinding.inflate(inflater, container, false)
        val activity = activity as? FileViewActivity
        activity?.hideTopBar()
        return binding.root
    }


}