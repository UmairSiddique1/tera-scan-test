package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.terascantest.R
import com.example.terascantest.databinding.FragmentEnterPinBinding
import com.example.terascantest.utils.Utils


class EnterPinFragment(val fileUri:String) : Fragment() {
    private lateinit var binding: FragmentEnterPinBinding
    private val editTextArray: ArrayList<EditText> = ArrayList(4)
    private lateinit var numTemp:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEnterPinBinding.inflate(inflater, container, false)
        val activity = activity as MainActivity
        activity.hideBottomNavigationView()
        activity.hideTopBar()



binding.pin.addTextChangedListener(object :TextWatcher{

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        var fileRename = s.toString()
        if (s != null) {
            if(s.length==4){
                if(fileRename.equals(context?.let { Utils.retrievePasswordValue(it,"password") })){
                    binding.tvIncorrectPin.visibility=View.GONE
                    startActivity(Intent(context, FileViewActivity::class.java))
                }
                else{
binding.tvIncorrectPin.visibility=View.VISIBLE
                  fileRename= null.toString()
                    binding.pin.text=null
                }
                Toast.makeText(context,fileUri,Toast.LENGTH_SHORT).show()

            }
        }
    }

})


        return binding.root
    }



}