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
    fun permissionDialog(activity: Activity, onAllowAction: () -> Unit) {
        val builder = AlertDialog.Builder(activity)
        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_permission_dialog, viewGroup, false)
        val tvDoNotAllow=dialogView.findViewById<TextView>(R.id.tv_do_not_allow)
        val tvAllow=dialogView.findViewById<TextView>(R.id.tv_allow)
        val tvTitle=dialogView.findViewById<TextView>(R.id.textview_title)
        val tvDesc=dialogView.findViewById<TextView>(R.id.textview_desc)

        tvDoNotAllow.setText(R.string.do_not_allow)
        tvAllow.setText(R.string.allow)
        tvTitle.setText(R.string.allow_storage_permission)
        tvDesc.setText(R.string.access_your_files)

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