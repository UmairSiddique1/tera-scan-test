package com.example.terascantest.ui.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.terascantest.R
import com.example.terascantest.interfaces.DialogDismissListenerCallBack


object PermissionAndAddDialog {
    @SuppressLint("MissingInflatedId")
    private lateinit var builder: AlertDialog.Builder
    fun permissionDialog(activity: Activity, onAllowAction: () -> Unit) {
        builder = AlertDialog.Builder(activity,R.style.CustomDialog)
        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_permission_dialog, viewGroup, false)
        val tvDoNotAllow = dialogView.findViewById<TextView>(R.id.tv_do_not_allow)
        val tvAllow = dialogView.findViewById<TextView>(R.id.tv_allow)
        val tvTitle = dialogView.findViewById<TextView>(R.id.textview_title)
        val tvDesc = dialogView.findViewById<TextView>(R.id.textview_desc)

        tvDoNotAllow.setText(R.string.do_not_allow)
        tvAllow.setText(R.string.allow)
        tvTitle.setText(R.string.allow_storage_permission)
        tvDesc.setText(R.string.access_your_files)

        builder.setView(dialogView)
        val alertDialog = builder.create()
        tvDoNotAllow.setOnClickListener {
            Toast.makeText(activity, "Permission not granted", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
        tvAllow.setOnClickListener {
            onAllowAction.invoke()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("MissingInflatedId")
    fun addDialog(activity: Activity, dismissListener: DialogDismissListenerCallBack): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val dialogView =
            LayoutInflater.from(activity).inflate(R.layout.layout_add_dialog, viewGroup, false)

        builder.setView(dialogView)
        val alertDialog = builder.create()

        val layoutParams = WindowManager.LayoutParams()

        layoutParams.copyFrom(alertDialog.window!!.attributes)

        // Set gravity to bottom and right
        layoutParams.gravity = Gravity.BOTTOM
        // Optional: Set a margin from the bottom of the screen if desired
        layoutParams.y = 450 // Example: Set 24dp margin from bottom
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.attributes = layoutParams
        alertDialog.setOnDismissListener {
            dismissListener.onDialogDismissed()
        }
        alertDialog.show()

        return alertDialog
    }


}