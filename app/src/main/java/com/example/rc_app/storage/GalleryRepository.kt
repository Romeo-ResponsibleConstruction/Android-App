package com.example.rc_app.storage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.rc_app.entity.Receipt
import java.io.*
import java.lang.Exception

class GalleryRepository(var context: Context) : InternalRepository<Receipt> {
    override fun saveToInternalStorage(filetype: Receipt): String {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val path = File(directory, "${System.nanoTime()}.png")

        try {
            val fos = FileOutputStream(path)
            fos.use {
                filetype.image.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
        } catch(e: Exception) {
            Log.e("repository-error", e.stackTraceToString())
        }
        return directory.absolutePath
    }

    override fun getFromInternalStorage(filename: String) {
        TODO("IMPLEMENT THIS, figure out how to represent")
    }

    override fun deleteFromInternalStorage(filename: String) {
        TODO("IMPLEMENT THIS")

    }
}