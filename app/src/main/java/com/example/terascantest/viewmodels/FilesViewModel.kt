package com.example.terascantest.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terascantest.model.FilesDataModel
import com.example.terascantest.utils.FetchFilesUtils
import kotlinx.coroutines.launch

class FilesViewModel : ViewModel() {
    private val _fileList = MutableLiveData<List<FilesDataModel>>()
    val fileList: LiveData<List<FilesDataModel>> get() = _fileList

    @RequiresApi(Build.VERSION_CODES.Q)
    fun fetchFiles(context: Context) {
        viewModelScope.launch {
            val updatedList = mutableListOf<FilesDataModel>()
            // Asynchronously fetch files
            FetchFilesUtils.fetchFiles(context, updatedList)
            // Update LiveData with the new list
            _fileList.value = updatedList
        }
    }
}