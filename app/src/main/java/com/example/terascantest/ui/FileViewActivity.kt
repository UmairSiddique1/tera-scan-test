package com.example.terascantest.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.terascantest.R
import com.example.terascantest.databinding.ActivityFileViewBinding
import com.example.terascantest.utils.Utils


class FileViewActivity : AppCompatActivity() {
private lateinit var binding:ActivityFileViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=intent
       val fileUri= intent.getStringExtra("fileUri").toString()
binding.ivBack.setOnClickListener { startActivity(Intent(applicationContext,MainActivity::class.java)) }

        val dialog = Dialog(this)

        binding.icLock.setOnClickListener{
            if(Utils.retrievePasswordValue(applicationContext,"password").isNotEmpty()){
                Utils.storeValIsLocked(applicationContext, "islocked$fileUri", true)
            }
            else{
                dialog.setContentView(R.layout.layout_password_dialog);
                val editTextPin=dialog.findViewById<EditText>(R.id.et_password)
                val textViewCancel=dialog.findViewById<TextView>(R.id.tv_pin_cancel)
                val textViewOk=dialog.findViewById<TextView>(R.id.tv_pin_ok)

                editTextPin.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        // Check if the EditText is empty
                        val fileRename = s.toString()
                        if (fileRename.isEmpty()) {
                            // Set the text color to A36DFF when EditText is empty
                            textViewOk.setTextColor(ContextCompat.getColor(applicationContext, R.color.secondary_title_color))
                        } else {
                            textViewOk.setTextColor(ContextCompat.getColor(applicationContext, R.color.select_text_color))

                            textViewOk.setOnClickListener {
                                Utils.loadFragment(this@FileViewActivity,SecurityQuestionsFragment(fileUri,fileRename))
                                dialog.dismiss()
                            }
                        }
                    }
                })
                textViewCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

        }
    }
    fun hideTopBar(){
        binding.rlTopbar.visibility=View.GONE
    }


}