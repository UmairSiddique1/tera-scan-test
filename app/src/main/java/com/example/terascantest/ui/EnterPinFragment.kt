package com.example.terascantest.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.terascantest.databinding.FragmentEnterPinBinding
import com.example.terascantest.utils.Utils

class EnterPinFragment(val fileUri: String) : Fragment() {
    private lateinit var binding: FragmentEnterPinBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentEnterPinBinding.inflate(inflater, container, false)
        val activity = activity as MainActivity
        activity.hideNavigation()
        binding.tvForgotPin.setOnClickListener {
            Utils.loadFragment(requireActivity(),SecurityQuestionsFragment(fileUri,""))
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(context,MainActivity::class.java))
            }
        })
        binding.pin.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val fileRename = s.toString()
                if (s != null) {
                    if (s.length == 4) {
                        if (fileRename == context?.let {Utils.retrievePasswordValue(it, "password")
                            }) {
                            binding.tvIncorrectPin.visibility = View.GONE
                            startActivity(Intent(context, FileViewActivity::class.java))
                        } else {
                            binding.tvIncorrectPin.visibility = View.VISIBLE
                            binding.pin.text = null
                        }
                    }
                }
            }
        })
        return binding.root
    }
}