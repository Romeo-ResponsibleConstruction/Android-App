package com.example.rc_app.entity.receipt

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.rc_app.storage.FileStorageRepository
import com.example.rc_app.storage.InternalFileStorageRepository
import com.example.rc_app.storage.Repository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReceiptFileRepository(val context: Context) : Repository<Receipt> {

    private val dir = "imageDir"
    private val fileStorageUtility: FileStorageRepository<Receipt> = InternalFileStorageRepository(context)

    private fun pathToReceipt(file: File): Receipt {
        val tailPath = file.name
        val (cal: String, uuid, _) = tailPath.split("_", ".")

        val date: Date? = SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(cal)
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

    override fun save(entity: Receipt): String {
        val compress: (FileOutputStream) -> Unit = { fos: FileOutputStream ->
            entity.image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }

        return fileStorageUtility.saveFile (
            dir,
            "${receiptToFilename(entity)}.png",
            compress
        )
    }

    override fun read(filepath: String): Receipt {
        return pathToReceipt(fileStorageUtility.getFile(dir, filepath))
    }

    override fun delete(filepath: String): Boolean {
        return fileStorageUtility.deleteFile(filepath)
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

    fun receiptToFilename(receipt: Receipt): String {
        return "${(receipt.datetimeToString())}_${receipt.id}.png"
    }
}