package com.example.rc_app.data.datasource

import java.io.File
import java.io.FileOutputStream

interface FileStorageDataSource<T> {
    fun saveFile(parentdir: String, filename: String, fosOperations: (input: FileOutputStream) -> Unit): String
    fun getFile(parentdir: String, filename: String): File
    fun deleteFile(parentdir: String, filename: String): Boolean
}