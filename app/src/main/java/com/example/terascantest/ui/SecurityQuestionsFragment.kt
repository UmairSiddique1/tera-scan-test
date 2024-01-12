package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.terascantest.R
import com.example.terascantest.databinding.FragmentSecurityQuestionsBinding
import com.example.terascantest.utils.Utils
import kotlin.properties.Delegates


class SecurityQuestionsFragment(val fileUri: String, val fileRename: String) : Fragment() {
    private lateinit var binding: FragmentSecurityQuestionsBinding
    var position: Int = 0
var answer:String=""
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSecurityQuestionsBinding.inflate(inflater, container, false)
        val activity = activity as? FileViewActivity
        activity?.hideTopBar()

        val spinnerData = listOf(
            getString(R.string.question_fav_color),
            getString(R.string.question_fav_color),
            getString(R.string.question_lucky_number)
        )
        val adapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                spinnerData
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter
binding.icDone.setOnClickListener {
    if(answer.isNotEmpty()){
        Utils.storeValIsLocked(requireContext(), "islocked$fileUri", true)
        Utils.storePasswordValue(requireContext(), "password", fileRename)
        Utils.storeQuestionValue(requireContext(), "ans$position$fileUri", answer)
        Toast.makeText(context,answer,Toast.LENGTH_SHORT).show()
        Log.d("ans",answer)
    }
}
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                this@SecurityQuestionsFragment.position = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }


        binding.etEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                this@SecurityQuestionsFragment.answer = s?.toString() ?: ""
                if (answer.isNotEmpty()) {
                    binding.icDone.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
                } else {
                    // Change the color back to the original color when EditText is empty
                    binding.icDone.setColorFilter(
                        ContextCompat.getColor(
                            context!!,
                            R.color.bottom_nav_unselected_color
                        )
                    )
                }
            }
        })
        return binding.root
    }
}