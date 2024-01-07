package com.example.terascantest.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.terascantest.R

object PermissionDialog {
    @SuppressLint("MissingInflatedId")
    fun alertDialog(activity: Activity,onAllowAction: () -> Unit) {
        val builder = AlertDialog.Builder(activity)
        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_permission_dialog, viewGroup, false)
        val tvDoNotAllow=dialogView.findViewById<TextView>(R.id.tv_do_not_allow)
        val tvAllow=dialogView.findViewById<TextView>(R.id.tv_allow)

        builder.setView(dialogView)
        val alertDialog = builder.create()
        tvDoNotAllow.setOnClickListener{
            Toast.makeText(activity,"Permission not granted",Toast.LENGTH_SHORT).show()
alertDialog.dismiss()
        }
        tvAllow.setOnClickListener {
         onAllowAction.invoke()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}