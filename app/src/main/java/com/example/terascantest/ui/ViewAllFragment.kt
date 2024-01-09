package com.example.terascantest.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.adapters.FilesAdapter
import com.example.terascantest.databinding.FragmentViewAllBinding
import com.example.terascantest.model.FilesDataModel
import com.example.terascantest.utils.FetchFilesUtils
import com.example.terascantest.viewmodels.FilesViewModel


class ViewAllFragment : Fragment() {
    private lateinit var binding: FragmentViewAllBinding
    private lateinit var filesViewModel: FilesViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewAllBinding.inflate(inflater, container, false)
        binding.icBackArrow.setOnClickListener {
            context?.startActivity(
                Intent(
                    context, MainActivity::class.java
                )
            )
        }
        filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]

        //HIDING THE BOTTOM NAVIGATION
        val activity = activity as? MainActivity
        activity?.hideBottomNavigationView()

        binding.rvViewAll.layoutManager = LinearLayoutManager(context)
        var list: MutableList<FilesDataModel> = ArrayList()
        val adapter = context?.let { FilesAdapter(it, emptyList(), "view_all") }
        binding.rvViewAll.adapter = adapter
        filesViewModel.fileList.observe(viewLifecycleOwner) { newFileList ->
            // Store the new data in the variable
            list = newFileList.toMutableList()
            // Update RecyclerView adapter with new data
            adapter?.updateList(newFileList)
        }

        // Fetch files when needed, for example, in onCreate or a button click
        context?.let { filesViewModel.fetchFiles(it) }
        adapter?.notifyDataSetChanged()

        val searchEditText =
            binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK)
        val closeIcon =
            binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(Color.BLACK)
        binding.searchView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                )//binding.searchView.layoutParams as LinearLayout.LayoutParams
                params.marginStart = 27.dpToPixels() // Convert dp to pixels
                binding.searchView.layoutParams = params
                binding.searchView.setBackgroundResource(R.drawable.bg_search_view)
                binding.tvTitle.visibility = View.INVISIBLE
            } else {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.marginEnd = 30.dpToPixels()
                binding.searchView.layoutParams = params
                binding.searchView.setBackgroundResource(android.R.color.transparent)
                binding.tvTitle.visibility = View.VISIBLE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList: ArrayList<FilesDataModel> = ArrayList()

                // Filter items only if there's a search query
                if (!newText.isNullOrEmpty()) {
                    for (item in list) {
                        if (item.name.lowercase().contains(newText.lowercase())) {
                            filteredList.add(item)
                        }
                    }
                }

                // Update recycler view and no data icon visibility based on filtered list
                if (filteredList.isEmpty()) {
                    binding.llNoData.visibility = View.VISIBLE
                    binding.rvViewAll.visibility = View.GONE // Hide the recycler view when empty
                } else {
                    binding.llNoData.visibility = View.GONE
                    binding.rvViewAll.visibility =
                        View.VISIBLE // Show the recycler view with filtered items
                    adapter?.filterList(filteredList)
                }

                return false
            }
        })

        return binding.root
    }

    private fun Int.dpToPixels(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}

