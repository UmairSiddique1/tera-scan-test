package com.example.terascantest.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.terascantest.R
import java.io.File

object DeleteDialog {
    @SuppressLint("MissingInflatedId")
    fun deleteDialog(context: Context, fileUri:Uri){
        val builder = AlertDialog.Builder(context)
        val viewGroup = (context as Activity).findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_permission_dialog, viewGroup, false)
        val tvTitle=dialogView.findViewById<TextView>(R.id.textview_title)
        val tvDesc=dialogView.findViewById<TextView>(R.id.textview_desc)
        val tvDelete=dialogView.findViewById<TextView>(R.id.tv_allow)
        val tvCancel=dialogView.findViewById<TextView>(R.id.tv_do_not_allow)
        tvTitle.setText(R.string.delete)
        tvDesc.setText(R.string.delete_desc)
        tvDelete.setText(R.string.yes)
        tvCancel.setText(R.string.cancel)

        builder.setView(dialogView)
        val alertDialog = builder.create()

        tvDelete.setOnClickListener {
            deleteFile(context,fileUri)
            alertDialog.dismiss()
        }
        tvCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun deleteFile(context: Context, uri: Uri): Boolean {
        val contentResolver: ContentResolver = context.contentResolver

        // Delete using MediaStore for Android 11+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val deletedRows = contentResolver.delete(uri, null, null)
            return deletedRows > 0
        }

        // For Android 10 and below, you can still use the old method
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // The file is not physically deleted, but it's removed from the MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            contentResolver.update(uri, contentValues, null, null)

            // Release the pending flag to actually delete the file
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)

            // Check if the file still exists in the MediaStore
            return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
            } ?: false
        }

        // If your app targets Android 9 (API level 28) or lower, you can use the traditional File API
        val filePath = getFilePathFromUri(context, uri)
        if (filePath != null) {
            val file = File(filePath)
            if (file.exists()) {
                return file.delete()
            }
        }

        return false
    }

    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }
}