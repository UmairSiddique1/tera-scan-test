package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.terascantest.R
import com.example.terascantest.StoragePermissions
import com.example.terascantest.databinding.FragmentHomeBinding
import com.example.terascantest.interfaces.DialogDismissListenerCallBack
import com.example.terascantest.viewmodels.FilesViewModel
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment(), DialogDismissListenerCallBack {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var filesViewModel: FilesViewModel
    private val fragmentMap = mutableMapOf<Int, Fragment>()
    private val permissionHandler: StoragePermissions by lazy {
        StoragePermissions(this, this)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.nestedSV.visibility = View.VISIBLE
        binding.frameLayout.visibility = View.GONE

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Recent Files"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Favourites"))
        fragmentMap[0] = RecentFragment()
        fragmentMap[1] = FavouriteFragment()

        tabLayoutSelectListener()

        filesViewModel = ViewModelProvider(requireActivity())[FilesViewModel::class.java]
        permissionHandler.requestStoragePermission()

        val animation = AnimationUtils.loadAnimation(
            context, R.anim.slide_down
        )
        binding.downArrow.startAnimation(animation)
        return binding.root
    }
    private fun showFragment(activity: FragmentActivity, fragment: Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun onDialogDismissed() {}

    override fun onShow() {
        binding.nestedSV.visibility = View.GONE
        binding.frameLayout.visibility = View.VISIBLE
        showFragment(requireActivity(), RecentFragment())
    }

    private fun tabLayoutSelectListener() {
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
    }

}