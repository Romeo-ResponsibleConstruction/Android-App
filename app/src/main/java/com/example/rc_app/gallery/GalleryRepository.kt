package com.example.rc_app.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.storage.GeneralFileRepository
import com.example.rc_app.storage.InternalRepository
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GalleryRepository(val context: Context) : GeneralFileRepository<Receipt>(context),
    InternalRepository<Receipt> {

    private val dir = "imageDir"

    private fun pathToReceipt(file: File): Receipt {
        val tailPath = file.name
        val (cal: String, uuid) = tailPath.split("_")

        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val date: Date? = df.parse(cal)
        date ?: throw IllegalStateException("date is null")
        val calendar = GregorianCalendar()
        calendar.time = date

        val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
        bitmap?.let {
            return Receipt(
                bitmap,
                calendar,
                UUID.fromString(uuid)
            )
        }
        throw IllegalStateException("bitmap is null")
    }

    override fun saveToInternalStorage(filetype: Receipt): String {
        val compress: (FileOutputStream) -> Unit = { fos: FileOutputStream ->
            filetype.image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }


        return saveFile(
            dir,
            "${(filetype.datetimeToString())}_${filetype.id}.png",
            compress
        )
    }

    override fun getFromInternalStorage(filepath: String): Receipt {
        return pathToReceipt(getFile("$dir/$filepath"))
    }

    fun getAllFromStorage(): List<Receipt> {
        val directory = File(context.filesDir, dir)
        val fileList = directory.listFiles()
        if (fileList != null) {
            return fileList.asSequence().map { pathToReceipt(it) }.toList()
        }
        return emptyList()
    }
}