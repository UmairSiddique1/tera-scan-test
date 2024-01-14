package com.example.terascantest.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.terascantest.R
import com.example.terascantest.dialogs.MoveFileBottomSheetDialog
import com.example.terascantest.dialogs.RenameDeleteDialog
import com.example.terascantest.interfaces.BottomSheetCallBack
import com.example.terascantest.model.ToolsItemsModel


class BottomSheetAdapter(
    private val context: Context,
    private val list: MutableList<ToolsItemsModel>,
    val fileUri: Uri,
    private val callBack: BottomSheetCallBack
) : BaseAdapter() {

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
            convertViewVar = LayoutInflater.from(context)
                .inflate(R.layout.layout_bottomsheet_items, parent, false)

            viewHolder = ViewHolder()

            viewHolder.iconImageView = convertViewVar.findViewById(R.id.iv_bottom_sheet)
            viewHolder.textView = convertViewVar.findViewById(R.id.tv_bottomsheet)
            viewHolder.llParent = convertViewVar.findViewById(R.id.ll_parent)

            convertViewVar.tag = viewHolder
        } else {
            viewHolder = convertViewVar.tag as ViewHolder
        }

        val currentItem = list[position]

        // Set the data to views
        viewHolder.iconImageView.setImageResource(currentItem.iconResId)
        viewHolder.textView.text = currentItem.name

        viewHolder.llParent.setOnClickListener {
            callBack.dismissBottomSheet()
            callBack.onItemClick(currentItem.name)
        }
        return convertViewVar!!
    }

    private class ViewHolder {
        lateinit var iconImageView: ImageView
        lateinit var textView: TextView
        lateinit var llParent: LinearLayout
    }

    // SHARING FILES
    private fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri)
    }

    private fun shareFile(context: Context, uri: Uri?, mimeType: String? = "*/*") {
        if (uri == null) return

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mimeType
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

        context.startActivity(Intent.createChooser(shareIntent, "Share File"))
    }

}