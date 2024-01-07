package com.example.terascantest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.terascantest.R
import com.example.terascantest.model.ToolsItemsModel


class ToolsAdapter(val context: Context, private val list:List<ToolsItemsModel>) :
    RecyclerView.Adapter<ToolsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView:ImageView
        val tvPremium:TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.tv_tools)
            imageView=view.findViewById(R.id.iv_tools)
            tvPremium=view.findViewById(R.id.tv_premium)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_tools, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = list[position].toolName
        viewHolder.imageView.setImageResource(list[position].iconResId)

        if(viewHolder.textView.text=="Edit PDF"){
            viewHolder.tvPremium.visibility=View.VISIBLE
        }
        else{
            viewHolder.tvPremium.visibility=View.GONE
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size

}
