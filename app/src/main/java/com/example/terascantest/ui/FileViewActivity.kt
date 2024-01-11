package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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

        val dialog = Dialog(this)
        Toast.makeText(applicationContext,fileUri,Toast.LENGTH_SHORT).show()

        binding.icLock.setOnClickListener{
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
                        textViewOk.setTextColor(ContextCompat.getColor(applicationContext, R.color.default_text_color))
                    } else {
                        textViewOk.setTextColor(ContextCompat.getColor(applicationContext, R.color.select_text_color))

                        textViewOk.setOnClickListener {
//                       Utils.loadFragment(this@FileViewActivity,SecurityQuestionsFragment())
                            storeValIsLocked("islocked$fileUri",true)

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
    fun hideTopBar(){
        binding.rlTopbar.visibility=View.GONE
    }

    @SuppressLint("CommitPrefEdits")
    fun storeValIsLocked(key:String,value:Boolean){
        val sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
}