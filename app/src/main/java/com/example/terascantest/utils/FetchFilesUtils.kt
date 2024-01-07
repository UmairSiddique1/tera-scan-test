package com.example.terascantest.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.terascantest.R
import com.example.terascantest.model.FilesDataModel
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object FetchFilesUtils {
//    @RequiresApi(Build.VERSION_CODES.R)
//    fun fetchPdfFiles(context: Context, pdfList:MutableList<FilesData>){
//        val projection = arrayOf(
//            MediaStore.Files.FileColumns.DISPLAY_NAME,
//            MediaStore.Files.FileColumns.DATE_MODIFIED,
//            MediaStore.Files.FileColumns.MEDIA_TYPE,
//            MediaStore.Files.FileColumns.MIME_TYPE,
//            MediaStore.Files.FileColumns._ID)
//
//        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT} AND ${MediaStore.Files.FileColumns.MIME_TYPE}='application/pdf'"
//
//        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"
//
//        val query = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            context.contentResolver.query(
//                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
//                projection,
//                selection,
//                null,
//                sortOrder
//            )
//        } else {
//            context. contentResolver.query(
//                MediaStore.Files.getContentUri("external"),
//                projection,
//                selection,
//                null,
//                sortOrder
//            )
//        }
//
//        query?.use { cursor ->
//            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
//            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
//            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
//
//            while (cursor.moveToNext()) {
//                val name = cursor.getString(nameColumn)
//                val dateModified = cursor.getLong(dateColumn)
//                val uri = ContentUris.withAppendedId(
//                    MediaStore.Files.getContentUri("external"),
//                    cursor.getLong(idColumn)
//                )
//
//                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateModified * 1000))
//
//                // Load PDF icon here (you may need to implement this)
//
//                // Fetch number of pages using AndroidPdfViewer library
//
////                val pdfFileInfo =FilesData(name, uri, R.drawable.ic_gallery, date, 12)
//                val pdfFileInfo=FilesData(name,"12 pages",date, R.drawable.ic_editpdf,uri)
//                pdfList.add(pdfFileInfo)
//            }
//        }
//    }


//    @RequiresApi(Build.VERSION_CODES.R)
//    fun fetchFiles(
//        context: Context,
//        fileList: MutableList<FilesData>,
//        mimeType: String,
//        iconResource: Int
//    ) {
//        val projection = arrayOf(
//            MediaStore.Files.FileColumns.DISPLAY_NAME,
//            MediaStore.Files.FileColumns.DATE_MODIFIED,
//            MediaStore.Files.FileColumns.MEDIA_TYPE,
//            MediaStore.Files.FileColumns.MIME_TYPE,
//            MediaStore.Files.FileColumns._ID)
//
//        // Adjust the selection based on whether a specific MIME type is provided
//        val selection = if (mimeType.isNotEmpty()) {
//            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT} AND ${MediaStore.Files.FileColumns.MIME_TYPE}=?"
//        } else {
//            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT}"
//        }
//        val selectionArgs = if (mimeType.isNotEmpty()) arrayOf(mimeType) else null
//
//        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"
//
//        val query = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            context.contentResolver.query(
//                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//            )
//        } else {
//            context.contentResolver.query(
//                MediaStore.Files.getContentUri("external"),
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//            )
//        }
//
//        query?.use { cursor ->
//            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
//            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
//            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
//
//            while (cursor.moveToNext()) {
//                val name = cursor.getString(nameColumn)
//                val dateModified = cursor.getLong(dateColumn)
//                val uri = ContentUris.withAppendedId(
//                    MediaStore.Files.getContentUri("external"),
//                    cursor.getLong(idColumn)
//                )
//
//                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateModified * 1000))
//
//                val pdfFileInfo=FilesData(name,"12 pages",date, R.drawable.ic_editpdf,uri) // 0 for page count or you can calculate it based on the file type
//                fileList.add(pdfFileInfo)
//            }
//        }
//    }



    @RequiresApi(Build.VERSION_CODES.Q)
    fun fetchFiles(
        context: Context,
        fileList: MutableList<FilesDataModel>
    ) {
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns._ID)

//        var pageCount:String
//        var bitmap:Bitmap?

        // Selection to fetch all documents
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT}"

        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"



        val queryUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            }
            else -> {
                MediaStore.Files.getContentUri("external")
            }
        }

        val query = context.contentResolver.query(
            queryUri,
            projection,
            selection,
            null, // No selection args needed for this query
            sortOrder
        )

        query?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val dateModified = cursor.getLong(dateColumn)
                val uri = ContentUris.withAppendedId(
                    queryUri,
                    cursor.getLong(idColumn)
                )

                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateModified * 1000))

//              if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)).equals("application/pdf", ignoreCase = true)) {
//                    pageCount= getPDFPageCount(context,uri).toString()
//                    bitmap= getPdfIcon(context,uri)
//                }
//else if(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)).equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ignoreCase = true)){
//                  pageCount=getPDFPageCount(context,uri).toString()
//                  bitmap=getPdfIcon(context,uri)
//              }
//              else {
//                  pageCount="N/A"
//                  bitmap=null
//                }

                val fileInfo=FilesDataModel(name,"",date, R.drawable.ic_pdf,uri)
                fileList.add(fileInfo)
            }
        }
    }

    // Function to get the number of pages in a PDF file using PdfRenderer
    private fun getPDFPageCount(context: Context, uri: Uri): Int {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")

        return try {
            parcelFileDescriptor?.use {
                // Use PdfRenderer from the Android SDK
                val pdfRenderer = PdfRenderer(it)
                val pageCount = pdfRenderer.pageCount
                pageCount
            } ?: -1 // Return -1 to indicate an error if parcelFileDescriptor is null
        } catch (e: IOException) {
            -1 // Return -1 to indicate an error in case of IO exception
        }
    }



    @SuppressLint("Recycle")
    fun getPdfIcon(context: Context, fileUri: Uri): Bitmap? {
        val contentResolver = context.contentResolver
        val pfd = try {
            contentResolver.openFileDescriptor(fileUri, "r")
        } catch (e: FileNotFoundException) {

            return null
        }

        val renderer = pfd?.let { PdfRenderer(it) }

        val page = renderer?.openPage(0)

        val bitmap: Bitmap? = try {
            val width = 128 // Adjust as needed for icon size
            val height = ((width * page?.height!!) ?: 0) / page.width
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
                page.render(it, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            }
        } catch (e: NullPointerException) {
            null
        } finally {
            page?.close()
            renderer?.close()
        }
        return bitmap
    }




//    private fun getPageCountFromWordDocument(context: Context, fileUri: Uri): Int? {
//        try {
//            val contentResolver: ContentResolver = context.contentResolver
//            val inputStream: InputStream? = try{
//                contentResolver.openInputStream(fileUri)
//            }catch (e:FileNotFoundException){
//                return null
//            }
//
//            if (inputStream != null) {
//                val document = XWPFDocument(inputStream)
//
//                // Counting the number of paragraphs, which is a rough estimate of pages
//                val numberOfParagraphs = document.paragraphs.size
//
//                // Adjust the count based on your specific document formatting and content
//                val estimatedPages = numberOfParagraphs / 5
//
//                // Close the input stream
//                inputStream.close()
//
//                return estimatedPages
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return -1 // Return -1 to indicate an error
//    }
//
//    private fun getIconFromWordDocument(context: Context, fileUri: Uri): Bitmap? {
//        try {
//            val contentResolver: ContentResolver = context.contentResolver
//            val inputStream: InputStream? = try{
//                contentResolver.openInputStream(fileUri)
//            }
//            catch (e:Exception){
//                return null
//            }
//
//            if (inputStream != null) {
//                val document = XWPFDocument(inputStream)
//
//                // Get the first picture data (assuming it's an image)
//                val pictureData: XWPFPictureData? = document.allPictures.firstOrNull()
//
//                if (pictureData != null) {
//                    // Convert the picture data to a Bitmap
//                    val imageBytes: ByteArray = pictureData.data
//                    val imageInputStream = ByteArrayInputStream(imageBytes)
//                    val bitmap = BitmapFactory.decodeStream(imageInputStream)
//
//                    // Close the input streams
//                    inputStream.close()
//                    imageInputStream.close()
//
//                    return bitmap
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return null // Return null to indicate an error or no icon found
//    }



}