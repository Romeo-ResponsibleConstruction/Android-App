package com.example.rc_app.entity.receipt

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.rc_app.infrastructure.GeneralFileRepository
import com.example.rc_app.infrastructure.Repository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReceiptRepository(val context: Context, val repository: Repository<Receipt>) : GeneralFileRepository<Receipt>(context), {

    private val dir = "imageDir"

    private fun pathToReceipt(file: File): Receipt {
        val tailPath = file.name
        val (cal: String, uuid, _) = tailPath.split("_", ".")

        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val date: Date? = df.parse(cal)
        date ?: throw IllegalStateException("date is null")
        val calendar = GregorianCalendar()
        calendar.time = date

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = BitmapFactory.decodeStream(FileInputStream(file), null, options)

        return Receipt(
            bitmap ?: throw IllegalStateException("bitmap is null"),
            calendar,
            UUID.fromString(uuid)
        )

    }

    override fun create(filetype: Receipt): String {
        val compress: (FileOutputStream) -> Unit = { fos: FileOutputStream ->
            filetype.image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }



        return saveFile(
            dir,
            "${(filetype.datetimeToString())}_${filetype.id}.png",
            compress
        )
    }

    override fun read(filepath: String): Receipt {
        return pathToReceipt(getFile(dir, filepath))
    }

    fun getAllFromStorage(): List<Receipt> {
        val cw = ContextWrapper(context)
        val directory: File = cw.getDir(dir, Context.MODE_PRIVATE)
        val fileList = directory.listFiles()
        if (fileList != null) {
            return fileList.asSequence().map { pathToReceipt(it) }.toList()
        }

        return emptyList()
    }
}