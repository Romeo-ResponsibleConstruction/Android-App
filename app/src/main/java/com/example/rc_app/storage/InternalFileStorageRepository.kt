package com.example.rc_app.infrastructure

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class InternalFileStorageRepository<T>(private val context: Context) : FileStorageRepository<T> {
    override fun saveFile(
        parentdir: String,
        filename: String,
        fosOperations: (input: FileOutputStream) -> Unit
    ): String {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir(parentdir, Context.MODE_PRIVATE)
        val path = File(directory, filename)

        try {
            val fos = FileOutputStream(path)
            fos.use {
                fosOperations(fos)
            }
        } catch (e: Exception) {
            Log.e("repository-error", e.stackTraceToString())
        }
        return directory.absolutePath
    }

    override fun getFile(parentdir: String, filename: String): File {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir(parentdir, Context.MODE_PRIVATE)

        return File(directory, filename)
    }

    override fun deleteFile(filepath: String): Boolean {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir(filepath, Context.MODE_PRIVATE)
        val file = File(directory, filepath)
        return file.delete()
    }
}