package com.example.terascantest.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.terascantest.R
import com.example.terascantest.adapters.BottomSheetAdapter
import com.example.terascantest.interfaces.BottomSheetCallBack
import com.example.terascantest.model.ToolsItemsModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(val fileUri: Uri) : BottomSheetDialogFragment(), BottomSheetCallBack {
    private lateinit var listView: ListView
    private lateinit var adapter: BottomSheetAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_bottomsheet, container, false)
        listView = view.findViewById(R.id.listview)
        val tvTitle = view.findViewById<TextView>(R.id.tv_bottomsheet_title)
        tvTitle.setText(R.string.file_name)

        adapter =
            context?.let { BottomSheetAdapter(it, itemsList().toMutableList(), fileUri, this) }!!

        listView.adapter = adapter

        return view
    }

    override fun dismissBottomSheet() {
        dismiss()
    }

    override fun onItemClick(s: String) {
        if (s == "Rename") {
            context?.let { RenameDeleteDialog.renameDialog(it, fileUri) }
            Log.d("items","Rename")
        } else if (s == "Share") {
            val mimeType: String = context?.let { getMimeType(it, fileUri) } ?: "*/*"
            context?.let { shareFile(it, fileUri, mimeType) }
            Log.d("items","Share")
        }
        else if(s=="Delete"){
            context?.let { RenameDeleteDialog.deleteDialog(it, fileUri) }
            Log.d("items","Delete")
            Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show()
        }
        else if(s=="Move"){
            val moveFileBottomSheet = MoveFileBottomSheetDialog(fileUri)
            moveFileBottomSheet.show((context as FragmentActivity).supportFragmentManager, "Bottom Sheet")
            Log.d("items","Move")
            Toast.makeText(context,"Move",Toast.LENGTH_SHORT).show()
        }
    }


    private fun itemsList(): List<ToolsItemsModel> {
        return listOf(
            ToolsItemsModel("Rename", R.drawable.ic_bottom_sheet_rename),
            ToolsItemsModel("Change Pin", R.drawable.ic_bottom_sheet_changepin),
            ToolsItemsModel("Share", R.drawable.ic_bottom_sheet_share),
            ToolsItemsModel("Save", R.drawable.ic_bottom_sheet_save),
            ToolsItemsModel("Move", R.drawable.ic_bottom_sheet_move),
            ToolsItemsModel("Delete", R.drawable.ic_bottom_sheet_delete)
        )
    }

    private fun shareFile(context: Context, uri: Uri?, mimeType: String? = "*/*") {
        if (uri == null) return
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mimeType
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, "Share File"))
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri)
    }
}