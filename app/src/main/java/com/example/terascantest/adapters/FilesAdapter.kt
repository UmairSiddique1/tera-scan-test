package com.example.terascantest.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.terascantest.R
import com.example.terascantest.dialogs.BottomSheetDialog
import com.example.terascantest.model.FilesDataModel


class FilesAdapter(val context: Context, private var list:List<FilesDataModel>, private val itemsToShow:String?):RecyclerView.Adapter<FilesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_files_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item=list[position]
        holder.name.text=item.name
        holder.date.text=item.date

holder.fileImg.setImageResource(item.iconRes)

        holder.fileMenuIcon.setOnClickListener{
val bottomSheet= BottomSheetDialog()
            bottomSheet.show((context as FragmentActivity).supportFragmentManager,"Bottom Sheet")
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<FilesDataModel>) {
        list = filterList
        notifyDataSetChanged()
    }

    fun updateList(newList: List<FilesDataModel>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return if(itemsToShow.equals("view_all")){
            list.size
        } else if(itemsToShow.equals("limit") && list.size>30){
            30
        } else{
            list.size
        }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val name:TextView
      val date:TextView

      val fileImg:ImageView
        val fileMenuIcon:ImageView

        init {
            // Define click listener for the ViewHolder's View
        name=view.findViewById(R.id.tv_fileName)
           date=view.findViewById(R.id.tv_fileDate)
            fileImg=view.findViewById(R.id.iv_filesImg)
           fileMenuIcon=view.findViewById(R.id.iv_fileMenuIcon)

        }
    }

}