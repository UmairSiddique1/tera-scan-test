package com.example.terascantest

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.terascantest.interfaces.DialogDismissListenerCallBack
import com.example.terascantest.ui.dialogs.PermissionAndAddDialog


class StoragePermissions(private val fragment: Fragment, private val callBack:DialogDismissListenerCallBack) {

    private lateinit var storageActivityResultLauncher: ActivityResultLauncher<Intent>

    init {
        initializeActivityResultLauncher()
    }

    private fun initializeActivityResultLauncher() {
        storageActivityResultLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleStoragePermissionResult(result.resultCode)
            }
    }

    private fun handleStoragePermissionResult(resultCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            // Storage permission granted
            showToast("Storage ppppermission granted.")
            callBack.onShow()
        } else {
            // Storage permission denied
            showToast("Storage permission denied.")
            callBack.onDialogDismissed()

        }
    }

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                PermissionAndAddDialog.permissionDialog(fragment.requireActivity(), onAllowAction = {
                    // Request the permission using the new approach for Android 11 and above
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", fragment.requireActivity().packageName, null)
                    intent.data = uri
                    storageActivityResultLauncher.launch(intent)
                })
            } else {
                // Storage permission already granted for Android 11 and above
                showToast("Storage permission already granted.")
                callBack.onShow()
            }
        } else {
            // Request the permission using the old approach for Android versions below 11
            requestLegacyStoragePermission()
        }
    }

    private fun requestLegacyStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Storage permission already granted for Android versions below 6.0
            showToast("Storage permission already granted.")
        } else {
            // Request the storage permission for Android versions 6.0 and above
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        const val STORAGE_PERMISSION_REQUEST_CODE = 1001
    }

    private fun showToast(message: String) {
        Toast.makeText(fragment.requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}