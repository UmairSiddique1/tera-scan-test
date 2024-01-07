package com.example.terascantest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.terascantest.R
import com.example.terascantest.model.ToolsItemsModel


class BottomSheetAdapter(private val context: Context,private val list: MutableList<ToolsItemsModel>):BaseAdapter() {


    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertViewVar = convertView

        if (convertViewVar == null) {
            convertViewVar = LayoutInflater.from(context).inflate(R.layout.layout_bottomsheet_items, parent, false)

            viewHolder = ViewHolder()

                viewHolder.iconImageView = convertViewVar.findViewById(R.id.iv_bottom_sheet)
                viewHolder.textView = convertViewVar.findViewById(R.id.tv_bottomsheet)




            convertViewVar.tag = viewHolder
        } else {
            viewHolder = convertViewVar.tag as ViewHolder
        }

        val currentItem = list[position]

        // Set the data to views
        viewHolder.iconImageView.setImageResource(currentItem.iconResId)
        viewHolder.textView.text = currentItem.toolName

        return convertViewVar!!
    }

    private class ViewHolder {
        lateinit var iconImageView: ImageView
        lateinit var textView: TextView
    }
}