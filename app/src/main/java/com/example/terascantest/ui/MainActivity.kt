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

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedLayout: LinearLayout
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadFragment(HomeFragment())

binding.bottomNavigation.setOnItemSelectedListener {item->
    when(item.itemId){
        R.id.item_home->{
            loadFragment(HomeFragment())
            true
        }
        R.id.item_docs->{
            loadFragment(DocsFragment())
            true
        }
        R.id.item_tools->{
            loadFragment(ToolsFragment())
            true
        }
        R.id.item_settings->{
            loadFragment(SettingsFragment())
            true
        }
        else->{
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


        //REQUEST PERMISSIONS FOR ANDROID 11 AND ABOVE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            PermissionDialog.permissionDialog(this, onAllowAction = {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            })
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment is ViewAllFragment) {
            // Pass the callback to the fragment

        }
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

    fun hideNavigation() {
        binding.bottomNavigation.visibility = View.GONE
        binding.llTopbar.visibility=View.GONE
    }
}