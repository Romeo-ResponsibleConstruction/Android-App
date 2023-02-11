package com.example.rc_app.storage

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

abstract class GeneralFileRepository<T>(private val context: Context) : InternalRepository<T> {
    protected fun saveFile(parentdir: String, filename: String, fosOperations: (input: FileOutputStream) -> Unit): String {
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

    protected fun getFile(filepath: String): File {
        return File(context.filesDir, filepath)
    }

    override fun deleteFromInternalStorage(filepath: String): Boolean {
        val dir = context.filesDir
        val file = File(dir, filepath)
        return file.delete()
    }
}