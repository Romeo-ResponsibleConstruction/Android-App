package com.example.rc_app.storage

import java.io.File
import java.io.FileOutputStream

interface FileStorageRepository<T> {
    fun saveFile(parentdir: String, filename: String, fosOperations: (input: FileOutputStream) -> Unit): String
    fun getFile(parentdir: String, filename: String): File
    fun deleteFile(parentdir: String, filename: String): Boolean
}