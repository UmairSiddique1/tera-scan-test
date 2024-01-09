package com.example.terascantest.dialogs

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.example.terascantest.R
import com.example.terascantest.adapters.BottomSheetAdapter
import com.example.terascantest.interfaces.BottomSheetCallBack
import com.example.terascantest.model.ToolsItemsModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(val fileUri:Uri): BottomSheetDialogFragment() ,BottomSheetCallBack{
    private lateinit var listView: ListView
    private lateinit var adapter: BottomSheetAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_bottomsheet, container, false)
listView=view.findViewById(R.id.listview)
val tvTitle=view.findViewById<TextView>(R.id.tv_bottomsheet_title)
        tvTitle.setText(R.string.file_name)


        val yourDataList = listOf(
           ToolsItemsModel("Rename", R.drawable.ic_bottom_sheet_rename),
           ToolsItemsModel("Change Pin", R.drawable.ic_bottom_sheet_changepin),
            ToolsItemsModel("Share", R.drawable.ic_bottom_sheet_share),
            ToolsItemsModel("Save", R.drawable.ic_bottom_sheet_save),
            ToolsItemsModel("Move", R.drawable.ic_bottom_sheet_move),
            ToolsItemsModel("Delete", R.drawable.ic_bottom_sheet_delete))

        adapter= context?.let { BottomSheetAdapter(it,yourDataList.toMutableList(),fileUri,this) }!!

listView.adapter=adapter

        return view
    }

    override fun dismissBottomSheet() {
    dismiss()
    }
}