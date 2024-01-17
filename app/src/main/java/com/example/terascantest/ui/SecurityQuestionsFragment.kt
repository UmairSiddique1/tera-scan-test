package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.terascantest.R
import com.example.terascantest.databinding.FragmentSecurityQuestionsBinding
import com.example.terascantest.utils.Utils


class SecurityQuestionsFragment(val fileUri: String, private val fileRename: String) : Fragment() {
    private lateinit var binding: FragmentSecurityQuestionsBinding
    private var position: Int = 0
    private var answer: String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSecurityQuestionsBinding.inflate(inflater, container, false)
        //HIDING TOPBAR
        val activity = activity as? FileViewActivity
        activity?.hideTopBar()
        //ON BACK PRESS LISTENER
        onBackPress()
        val adapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, list()) }

        adapter?.setDropDownViewResource(R.layout.layout_spinner_dropdown)

        binding.spinner.adapter = adapter
        Toast.makeText(context, context?.let { Utils.retrievePasswordValue(it, "password") }, Toast.LENGTH_SHORT).show()
        binding.icDone.setOnClickListener {
            if (context?.let { it1 -> Utils.retrieveQuestionValue(it1, "ans").toString().isEmpty() } == true && answer.isNotEmpty()) {
                Utils.storeValIsLocked(requireContext(), "islocked$fileUri", true)
                Utils.storePasswordValue(requireContext(), "password", fileRename)
                Utils.storeQuestionValue(requireContext(), "ans", answer)
                Utils.storeQuestionPosition(requireContext(), "position", position)
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                if (answer == Utils.retrieveQuestionValue(requireContext(), "ans")) {

                    Toast.makeText(context, "forgot pin", Toast.LENGTH_SHORT).show()
                    alertDialog()
                } else {
                    Toast.makeText(context, "Unable to forgot", Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                this@SecurityQuestionsFragment.position = position
                (view as? TextView)?.setTextColor(resources.getColor(R.color.black,null))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }


        binding.etEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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

    private fun list(): List<String> {
        val list = listOf(
            getString(R.string.question_fav_color),
            getString(R.string.question_pet_name),
            getString(R.string.question_lucky_number)
        )

        val position = context?.let { Utils.retrieveQuestionPosition(it, "position") }


        if (context?.let { Utils.retrieveQuestionValue(it, "ans").isNotEmpty() } == true) {
            // If there is a value for the question at the specified position, return only that item
            return listOf(list[position!!])
        } else {
            // Otherwise, return the entire list
            return list
        }
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(context, MainActivity::class.java))
            }
        })
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val viewGroup = requireActivity().findViewById<ViewGroup>(android.R.id.content)

        val dialogView = LayoutInflater.from(requireActivity())
            .inflate(R.layout.layout_new_pin, viewGroup, false)
        val etEnterNewPin = dialogView.findViewById<EditText>(R.id.et_new_pin)
        val etConfirmPin = dialogView.findViewById<EditText>(R.id.et_confirm_pin)
        val tvDone = dialogView.findViewById<TextView>(R.id.tv_new_pin)
        val tvCancel = dialogView.findViewById<TextView>(R.id.tv_cancel_newpin)


        builder.setView(dialogView)
        val alertDialog = builder.create()

        tvDone.setOnClickListener {
            val newPin = etEnterNewPin.text.toString()
            val confirmPin = etConfirmPin.text.toString()
            if (newPin.isNotEmpty() && confirmPin.isNotEmpty()) {
                if (newPin == confirmPin) {
                    context?.let { it1 -> Utils.storePasswordValue(it1, "password", newPin) }
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context, "Please write same pins", Toast.LENGTH_SHORT).show()
                }
            }
        }
        tvCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        builder.setView(dialogView)

        alertDialog.show()
    }
}