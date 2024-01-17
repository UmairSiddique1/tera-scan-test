package com.example.terascantest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terascantest.R
import com.example.terascantest.StoragePermissions
import com.example.terascantest.adapters.ToolsAdapter
import com.example.terascantest.databinding.FragmentHomeBinding
import com.example.terascantest.interfaces.BackgroundOpacityCallback
import com.example.terascantest.ui.dialogs.PermissionAndAddDialog
import com.example.terascantest.interfaces.DialogDismissListenerCallBack
import com.example.terascantest.model.ToolsItemsModel
import com.example.terascantest.viewmodels.FilesViewModel
import com.google.android.material.tabs.TabLayout


class HomeFragment(val backgroundOpacityCallback: BackgroundOpacityCallback) : Fragment(),
    DialogDismissListenerCallBack {

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
        onBackPress()
        binding.nestedSV.visibility = View.VISIBLE
        binding.frameLayout.visibility = View.GONE

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Recent Files"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Favourites"))
        fragmentMap[0] = RecentFragment()
        fragmentMap[1] = FavouriteFragment()

        tabLayoutSelectListener()

        filesViewModel = ViewModelProvider(requireActivity())[FilesViewModel::class.java]
        permissionHandler.requestStoragePermission()

        binding.ivDialog.setOnClickListener {
//            var dialog = PermissionAndAddDialog.addDialog(requireActivity(), this)
backgroundOpacityCallback.onDisplay()
            binding.rlAddDialog.visibility = View.VISIBLE
//binding.framelayout2.background= context?.let { it1 -> ContextCompat.getDrawable(it1,R.color.opacity) }
            binding.ivDialog.startAnimation(rotateAnimation(0f, 45f, 200))
//            dialog.show()
        }


        val animation = AnimationUtils.loadAnimation(
            context, R.anim.slide_down
        )
        binding.downArrow.startAnimation(animation)
        return binding.root
    }


//    private fun getToolItemList(): List<ToolsItemsModel> {
//        return listOf(
//            ToolsItemsModel("Edit PDF", R.drawable.ic_editpdf),
//            ToolsItemsModel("Image to PDF", R.drawable.ic_imagetopdf),
//            ToolsItemsModel("Scan Id", R.drawable.ic_scanid),
//            ToolsItemsModel("Merge PDF", R.drawable.ic_mergepdf)
//        )
//    }

    private fun showFragment(activity: FragmentActivity, fragment: Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun onDialogDismissed() {
        Toast.makeText(context, "hhhh", Toast.LENGTH_SHORT).show()

        binding.ivDialog.startAnimation(rotateAnimation(45f, 0f, 200))
    }

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

    private fun rotateAnimation(
        fromDegree: Float,
        toDegree: Float,
        duration: Long
    ): RotateAnimation {
        val pivotX = binding.ivDialog.width / 2.0f
        val pivotY = binding.ivDialog.height / 2.0f

        val rotateAnimation = RotateAnimation(
            fromDegree,
            toDegree,
            RotateAnimation.RELATIVE_TO_SELF,
            pivotX / binding.ivDialog.width,  // pivotX as a fraction of the view's width
            RotateAnimation.RELATIVE_TO_SELF,
            pivotY / binding.ivDialog.height   // pivotY as a fraction of the view's height
        )

        rotateAnimation.duration = duration
        rotateAnimation.fillAfter = true

        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
            }
        })

        return rotateAnimation
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backgroundOpacityCallback.onDismiss()
            }
        })
    }

}