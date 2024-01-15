package com.example.terascantest.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.StoragePermissions
import com.example.terascantest.adapters.FilesAdapter
import com.example.terascantest.adapters.ToolsAdapter
import com.example.terascantest.databinding.FragmentHomeBinding
import com.example.terascantest.dialogs.HomeDialogs
import com.example.terascantest.interfaces.BottomSheetCallBack
import com.example.terascantest.interfaces.DialogDismissListenerCallBack
import com.example.terascantest.model.FilesDataModel
import com.example.terascantest.model.ToolsItemsModel
import com.example.terascantest.utils.Utils
import com.example.terascantest.viewmodels.FilesViewModel
import com.google.android.material.tabs.TabLayout
import kotlin.properties.Delegates


class HomeFragment : Fragment(),DialogDismissListenerCallBack{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var filesViewModel: FilesViewModel
    private val fragmentMap = mutableMapOf<Int, Fragment>()
    private val permissionHandler: StoragePermissions by lazy {
        StoragePermissions(this)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        showFragment(requireActivity(), RecentFragment())
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Recent Files"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Favourites"))
        fragmentMap[0] = RecentFragment()
        fragmentMap[1] = FavouriteFragment()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = fragmentMap[tab.position] ?: return
                // Show fragment in the container
                showFragment(requireActivity(), fragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Handle unselected tab if needed
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle reselected tab if needed
            }
        })

        filesViewModel = ViewModelProvider(requireActivity())[FilesViewModel::class.java]
        permissionHandler.requestStoragePermission()
        val rotateAnimation = RotateAnimation(
            0f,
            45f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 200


        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation started
                binding.ivDialog.rotation =0f
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended
                binding.ivDialog.rotation = 45f

            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
            }
        })

        binding.ivDialog.setOnClickListener {
            var dialog=HomeDialogs.addDialog(requireActivity(),this)
            binding.ivDialog.startAnimation(rotateAnimation)
       dialog.show()
        }

        val toolLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTools.layoutManager = toolLayoutManager
        val toolItemList = getToolItemList()
        val adapter = context?.let { ToolsAdapter(it, toolItemList) }
        binding.rvTools.adapter = adapter

        val animation = AnimationUtils.loadAnimation(
            context, R.anim.slide_down
        )
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

    private fun showFragment(activity: FragmentActivity, fragment: Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun onDialogDismissed() {
        binding.ivDialog.rotation=0f
    }
}