package com.example.terascantest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.terascantest.R
import com.example.terascantest.databinding.ActivityMainBinding
import com.example.terascantest.utils.Utils
import com.example.terascantest.viewmodels.FilesViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private lateinit var filesViewModel: FilesViewModel
    private var isIconClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        filesViewModel = ViewModelProvider(this)[FilesViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadFragment(HomeFragment())
        binding.ivDialog.setOnClickListener {
            if (!isIconClicked) {
                binding.framelayout2.background =
                    ContextCompat.getDrawable(applicationContext, R.color.opacity)
                binding.ivDialog.startAnimation(rotateAnimation(0f, 45f, 200))
                binding.llAddItems.visibility = View.VISIBLE
            } else {
                binding.framelayout2.background =
                    ContextCompat.getDrawable(applicationContext, R.color.transparent)
                binding.ivDialog.startAnimation(rotateAnimation(45f, 0f, 200))
                binding.llAddItems.visibility = View.GONE
            }
            isIconClicked = !isIconClicked
        }
        binding.llGallery.setOnClickListener {
            binding.framelayout2.background =
                ContextCompat.getDrawable(applicationContext, R.color.transparent)
            binding.ivDialog.startAnimation(rotateAnimation(45f, 0f, 200))
            binding.llAddItems.visibility = View.GONE
            isIconClicked = !isIconClicked

        }
        binding.llCamera.setOnClickListener {
            binding.framelayout2.background =
                ContextCompat.getDrawable(applicationContext, R.color.transparent)
            binding.ivDialog.startAnimation(rotateAnimation(45f, 0f, 200))
            binding.llAddItems.visibility = View.GONE
            isIconClicked = !isIconClicked

        }
        binding.ivSearch.setOnClickListener {
            Utils.loadFragment(this, ViewAllFragment())
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.item_docs -> {
                    loadFragment(DocsFragment())
                    true
                }

                R.id.item_tools -> {
                    loadFragment(ToolsFragment())
                    true
                }

                R.id.item_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }

                else -> {
                    false
                }
            }

        }


        //HANDLING PERMISSIONS FOR FILE READ AND WRITE

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request both read and write permissions
            STORAGE_PERMISSION_REQUEST_CODE
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentContainer, fragment)
        // Commit the transaction
        transaction.commit()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            // Check if the permission was granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform your read operation
                // ...
                Toast.makeText(applicationContext, "permission granted", Toast.LENGTH_SHORT).show()
                filesViewModel.fetchFiles(this)
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    fun hideNavigation() {
        binding.bottomNavigation.visibility = View.GONE
        binding.llTopbar.visibility = View.GONE
    }

    @SuppressLint("MissingInflatedId")


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
        return rotateAnimation
    }
}