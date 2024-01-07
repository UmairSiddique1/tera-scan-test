package com.example.terascantest.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.terascantest.R
import com.example.terascantest.databinding.ActivityMainBinding
import com.example.terascantest.dialogs.PermissionDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedLayout: LinearLayout
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        selectedLayout = binding.llHome
        loadFragment(HomeFragment())

        binding.llHome.setOnClickListener {
            handleLinearLayoutClick(binding.llHome, HomeFragment())
        }
        binding.llDocs.setOnClickListener {
            handleLinearLayoutClick(
                binding.llDocs,
                DocsFragment()
            )
        }
        binding.llTools.setOnClickListener {
            handleLinearLayoutClick(
                binding.llTools,
                ToolsFragment()
            )
        }
        binding.llSettings.setOnClickListener {
            handleLinearLayoutClick(
                binding.llSettings,
                SettingsFragment()
            )
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


        //REQUEST PERMISSIONS FOR ANDROID 11 AND ABOVE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            PermissionDialog.alertDialog(this, onAllowAction = {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            })
        }

    }

    private fun handleLinearLayoutClick(linearLayout: LinearLayout, fragment: Fragment) {
        if (linearLayout != selectedLayout) {
            updatePreviousLayout(selectedLayout)
            updateSelectedLayout(linearLayout)
            loadFragment(fragment)
        }
    }

    private fun updatePreviousLayout(linearLayout: LinearLayout) {
        // Reset the color and icon of the previous selected linear layout // Set your unselected background resource
        val icon = linearLayout.getChildAt(0) as ImageView
        val text = linearLayout.getChildAt(1) as TextView
        icon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.bottom_nav_unselected_color
            )
        ) // Set your unselected icon color
        text.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.bottom_nav_unselected_color
            )
        ) // Set your unselected text color
    }

    private fun updateSelectedLayout(linearLayout: LinearLayout) {
        // Set the color and icon for the newly selected linear layout
        // Set your selected background resource
        val icon = linearLayout.getChildAt(0) as ImageView
        val text = linearLayout.getChildAt(1) as TextView
        icon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.bottom_nav_selected_color
            )
        ) // Set your selected icon color
        text.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.bottom_nav_selected_color
            )
        ) // Set your selected text color

        // Update the selectedLayout to the current linear layout
        selectedLayout = linearLayout
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentContainer, fragment)
        // Commit the transaction
        transaction.commit()
    }

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
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    fun hideBottomNavigationView() {
        binding.bottomNavLinearlayout.visibility = View.GONE
    }
}