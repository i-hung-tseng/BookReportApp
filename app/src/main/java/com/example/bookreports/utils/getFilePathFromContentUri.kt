package com.example.bookreports.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class getFilePathFromContentUri(contentUri: Uri,contentResolver: ContentResolver) {

    lateinit var filePaht: String
    val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor: Cursor? = contentResolver.query(contentUri,filePathColumn,null,null,null)



}