package com.example.terascantest.ui.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.terascantest.R
import com.example.terascantest.adapters.BottomSheetAdapter
import com.example.terascantest.interfaces.BottomSheetCallBack
import com.example.terascantest.model.ToolsItemsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoveFileBottomSheetDialog(private val fileUri: Uri) : BottomSheetDialogFragment(),
    BottomSheetCallBack {
    private lateinit var adapter: BottomSheetAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_bottomsheet, container, false)
        createFolder("Favourites")
        val tvTitle = view.findViewById<TextView>(R.id.tv_bottomsheet_title)
        val listView = view.findViewById<ListView>(R.id.listview)
        tvTitle.setText(R.string.move_to)

        val dataList = generateDataList()

        adapter = context?.let { BottomSheetAdapter(it, dataList.toMutableList(), fileUri, this) }!!
        listView.adapter = adapter
        return view
    }

    override fun dismissBottomSheet() {
        dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(s: String) {
        if (s == "Create new folder") {
            context?.let { createNewFolderDialog(it) }
        } else {
            val directoryPath = getExternalStorageDirectory().absolutePath + "/com.example.terascan/"
            val foldersInDirectory = listFoldersInDirectory(directoryPath)
            for (folders in foldersInDirectory.indices) {
                if (s == foldersInDirectory[folders].name) {
                    val directory = File(foldersInDirectory[folders].absolutePath)

                    if (directory.exists() && directory.isDirectory) {
                        moveFile(uriToFile(requireContext(), fileUri)!!, directory)
                    }
                }
            }
        }
    }

    // CREATE NEW FOLDER DIALOG
    @SuppressLint("MissingInflatedId")
    fun createNewFolderDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val viewGroup = (context as Activity).findViewById<ViewGroup>(android.R.id.content)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.layout_rename_dialog, viewGroup, false)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title_bottomsheet)
        val et_rename = dialogView.findViewById<EditText>(R.id.et_rename_file)
        val tv_rename = dialogView.findViewById<TextView>(R.id.tv_rename)
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)

        tvTitle.setText(R.string.create_new_folder)

        builder.setView(dialogView)
        val alertDialog = builder.create()
        et_rename.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("SdCardPath")
            override fun afterTextChanged(s: Editable?) {
                // Check if the EditText is empty
                val fileRename = s.toString()
                if (fileRename.isEmpty()) {
                    // Set the text color to A36DFF when EditText is empty
                    tv_rename.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.secondary_title_color
                        )
                    )
                } else {
                    // Set the default text color when EditText is not empty
                    tv_rename.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.select_text_color
                        )
                    )
                }

                tv_rename.setOnClickListener {

                    createFolder(fileRename)
                    alertDialog.dismiss()
                }
            }
        })
        tv_cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    // METHOD THAT RETURN DATA LIST
    @SuppressLint("SdCardPath")
    private fun generateDataList(): List<ToolsItemsModel> {
        val dataList = mutableListOf<ToolsItemsModel>()
        // Add "Create new folder" as the first item
        dataList.add(ToolsItemsModel("Create new folder", R.drawable.ic_new_folder))
        val directoryPath = getExternalStorageDirectory().absolutePath + "/com.example.terascan/"
        val foldersInDirectory = listFoldersInDirectory(directoryPath)
        // Add the folder names and paths to the list
        for (folders in foldersInDirectory.indices) {
            dataList.add(
                ToolsItemsModel(
                    foldersInDirectory[folders].name,
                    R.drawable.ic_new_folder,
                    foldersInDirectory[folders].absolutePath
                )
            )
        }
        return dataList
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun moveFile(src: File, dest: File) {
        if (!dest.isDirectory) {
            throw IllegalArgumentException("Destination must be a directory")
        }
        try {
            // Append timestamp to the file name to ensure uniqueness
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date())
            val destinationFileName = src.name

            // Copy to destination folder with the unique file name
            val destinationPath = dest.toPath().resolve(destinationFileName)
            Files.copy(src.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING)
            // Delete the source file
            Files.delete(src.toPath())

            Log.d("srcpath", src.toPath().toString() + " " + destinationPath)

            Log.d("movefile", "Successful")
        } catch (e: Exception) {
            Log.d("movefile", "Unable to move file: " + e.message.toString())
        }
    }
    private fun uriToFile(context: Context, uri: Uri): File? {
        try {
            val filePath: String? = getRealPathFromURI(context, uri)
            if (filePath != null) {
                return File(filePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        }
    }


    fun createFolder(fileName: String) {
        // Get the external storage directory for your app
        val externalStorageDir = getExternalStorageDirectory()
        // Specify the name of the folder you want to create
        val folderName = "com.example.terascan/$fileName/"
        // Create a File object representing the new folder
        val folder = File(externalStorageDir, folderName)

        // Check if the folder already exists
        if (!folder.exists()) {
            // If the folder doesn't exist, create it
            val isFolderCreated = folder.mkdirs()

            // Check if the folder creation was successful
            if (isFolderCreated) {
                println("Folder created successfully: ${folder.absolutePath}")
            } else {
                println("Failed to create folder")
            }
        } else {
            println("Folder already exists: ${folder.absolutePath}")
        }
    }

    private fun listFoldersInDirectory(directoryPath: String): List<File> {
        val directory = File(directoryPath)
        // Check if the specified directory exists
        return if (directory.exists() && directory.isDirectory) {
            // List all files and directories in the specified directory
            val filesAndDirectories = directory.listFiles()
            // Filter only directories
            val folders = filesAndDirectories?.filter { it.isDirectory }
            // Return list of directories
            folders ?: emptyList()
        } else {
            println("Directory does not exist: $directoryPath")
            emptyList()
        }
    }
}