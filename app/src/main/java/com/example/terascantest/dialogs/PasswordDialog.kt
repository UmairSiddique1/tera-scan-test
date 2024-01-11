package com.example.terascantest.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.terascantest.R
import com.example.terascantest.ui.SecurityQuestionsFragment
import com.example.terascantest.utils.Utils

object PasswordDialog {
    @SuppressLint("MissingInflatedId")
    fun passwordDialog(context:Context){

            val dialogView = Dialog(context)
          dialogView.setContentView(R.layout.layout_password_dialog)

        dialogView.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogView.setCancelable(false);

            val editTextPin=dialogView.findViewById<EditText>(R.id.et_password)
            val textViewCancel=dialogView.findViewById<TextView>(R.id.tv_pin_cancel)
            val textViewOk=dialogView.findViewById<TextView>(R.id.tv_pin_ok)


            editTextPin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    // Check if the EditText is empty
                    val fileRename = s.toString()
                    if (fileRename.isEmpty()) {
                        // Set the text color to A36DFF when EditText is empty
                        textViewOk.setTextColor(ContextCompat.getColor(context, R.color.default_text_color))
                    } else {
                        textViewOk.setTextColor(ContextCompat.getColor(context, R.color.select_text_color))

                        textViewOk.setOnClickListener {
                            if(context is FragmentActivity){
                                Utils.loadFragment(context,SecurityQuestionsFragment())
                            }


                          dialogView.dismiss()
                        }
                    }
                }
            })
            textViewCancel.setOnClickListener {
                dialogView.dismiss()
            }

           dialogView.show()



    }
}