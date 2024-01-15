package com.example.terascantest.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.adapters.FilesAdapter
import com.example.terascantest.databinding.FragmentRecentBinding
import com.example.terascantest.viewmodels.FilesViewModel

class RecentFragment : Fragment() {
    private lateinit var binding: FragmentRecentBinding
    private lateinit var filesViewModel: FilesViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecentBinding.inflate(inflater, container, false)
        filesViewModel = ViewModelProvider(requireActivity())[FilesViewModel::class.java]
        binding.rvRecent.layoutManager = LinearLayoutManager(context)
        val filesAdapter = context?.let { FilesAdapter(it, emptyList(), "limit") }
        binding.rvRecent.adapter = filesAdapter

        filesViewModel.fileList.observe(viewLifecycleOwner) { newFileList ->

            if(newFileList.isNotEmpty()){
                binding.llRecent.visibility=View.GONE
                binding.rvRecent.visibility=View.VISIBLE
            }
            else{
                binding.llRecent.visibility=View.VISIBLE
                binding.rvRecent.visibility=View.GONE
            }
            filesAdapter?.updateList(newFileList)
        }

        context?.let { filesViewModel.fetchFiles(it) }
        filesAdapter?.notifyDataSetChanged()
        return binding.root
    }

}