package com.example.terascantest.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.adapters.FilesAdapter
import com.example.terascantest.adapters.ToolsAdapter
import com.example.terascantest.databinding.FragmentHomeBinding
import com.example.terascantest.model.FilesDataModel
import com.example.terascantest.model.ToolsItemsModel
import com.example.terascantest.utils.Utils
import com.example.terascantest.viewmodels.FilesViewModel


class HomeFragment : Fragment() {

private lateinit var binding: FragmentHomeBinding
    private lateinit var filesViewModel: FilesViewModel
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]

        binding.tvViewAll.setOnClickListener {
            Utils.loadFragment(requireActivity(),ViewAllFragment())
        }

        val toolLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTools.layoutManager = toolLayoutManager

        //Layout Manager for Files
        val filesLayoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvFiles.layoutManager=filesLayoutManager

        val filesAdapter= context?.let { FilesAdapter(it, emptyList(),"limit") }
        binding.rvFiles.adapter=filesAdapter
//        context?.let { FetchFilesUtils.fetchFiles(it,filesList) }
        filesViewModel.fileList.observe(viewLifecycleOwner) { newFileList ->

            if(newFileList.isNotEmpty()){
                binding.nestedSV.visibility=View.GONE
                binding.rvFiles.visibility=View.VISIBLE
            }
            else{
                binding.nestedSV.visibility=View.VISIBLE
                binding.rvFiles.visibility=View.GONE
            }
            // Update RecyclerView adapter with new data
           filesAdapter?.updateList(newFileList)
        }

        // Fetch files when needed, for example, in onCreate or a button click
        context?.let { filesViewModel.fetchFiles(it) }
        filesAdapter?.notifyDataSetChanged()




        val toolItemList = getToolItemList()
        val adapter = context?.let { ToolsAdapter(it, toolItemList) }
        binding.rvTools.adapter = adapter

        val animation = AnimationUtils.loadAnimation(
            context, R.anim.slide_down)
       binding.downArrow.startAnimation(animation)
        return binding.root
    }


    private fun getToolItemList(): List<ToolsItemsModel> {
        return listOf(
            ToolsItemsModel("Edit PDF", R.drawable.ic_editpdf),
            ToolsItemsModel("Image to PDF", R.drawable.ic_imagetopdf),
            ToolsItemsModel("Scan Id", R.drawable.ic_scanid),
            ToolsItemsModel("Merge PDF", R.drawable.ic_mergepdf)
        )
    }
}