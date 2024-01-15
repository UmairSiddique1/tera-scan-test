package com.example.terascantest.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.adapters.FilesAdapter
import com.example.terascantest.databinding.FragmentFavouriteBinding
import com.example.terascantest.model.FilesDataModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class FavouriteFragment : Fragment() {
    private lateinit var binding:FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavouriteBinding.inflate(inflater, container, false)
        binding.rvFavourites.layoutManager=LinearLayoutManager(context)
        val list:MutableList<FilesDataModel> =ArrayList()
        val favouritesFolder = File(Environment.getExternalStorageDirectory(), "com.example.terascan/Favourites/")
        val files = favouritesFolder.listFiles()
        if (files != null) {
            binding.llFavourites.visibility=View.GONE
            binding.rvFavourites.visibility=View.VISIBLE
            for (file in files) {
                val fileUri=Uri.fromFile(file)
                val lastModified=file.lastModified()
                val date = Date(lastModified)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy").format(date)
                list.add(FilesDataModel(file.name,"",formattedDate,R.drawable.ic_pdf,fileUri))
            }
        } else {
           binding.llFavourites.visibility=View.VISIBLE
            binding.rvFavourites.visibility=View.GONE
        }
val adapter= context?.let { FilesAdapter(it,list,"view_all") }
        binding.rvFavourites.adapter=adapter
        adapter?.notifyDataSetChanged()
        return binding.root
    }

}