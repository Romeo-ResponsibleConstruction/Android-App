package com.example.rc_app.data.datasource

import android.content.Context
import com.example.rc_app.entity.receipt.Receipt
import com.google.api.client.json.Json
import com.google.gson.Gson
import java.io.FileOutputStream
import java.util.*

class ReceiptLogDataSource(val context: Context) {

    private val parentDir = "receiptLogDir"
    private val jsonFilename = "log.json"
    private val fileStorageUtility: FileStorageDataSource<Receipt> =
        InternalFileStorageDataSource(context)

    private class JsonFormat(val week_beg: Long, var total: Int)

    fun save(): String {
//        create a gson object
        val gson = Gson()
        val initialFile = fileStorageUtility.getFile(parentDir, jsonFilename)
        val jsonFormat: JsonFormat = if (initialFile.exists()) {
            val jsonString = String(initialFile.inputStream().readBytes())
            gson.fromJson(jsonString, JsonFormat::class.java)
        } else {
            val c = Calendar.getInstance()
            c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            JsonFormat(c.time.time, 0)
        }

        jsonFormat.total += 1

        val writer: (FileOutputStream) -> Unit = { fos: FileOutputStream ->
            fos.write(gson.toJson(jsonFormat).toByteArray())
        }
        return fileStorageUtility.saveFile(
            parentDir, jsonFilename,
            writer
        )
    }

    fun getDate(): Int {
        val gson = Gson()
        val c = Calendar.getInstance()
        c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        val mon = c.time.time
        val initialFile = fileStorageUtility.getFile(parentDir, jsonFilename)
        val jsonFormat: JsonFormat = if (initialFile.exists()) {
            val jsonString = String(initialFile.inputStream().readBytes())
            gson.fromJson(jsonString, JsonFormat::class.java)
        } else {
            JsonFormat(mon, 0)
            return 0
        }
        return jsonFormat.total
    }

    fun onAppStart() {
        val gson = Gson()
        val c = Calendar.getInstance()
        c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        val mon = c.time.time
        val initialFile = fileStorageUtility.getFile(parentDir, jsonFilename)
        val jsonFormat: JsonFormat = if (initialFile.exists()) {
            val jsonString = String(initialFile.inputStream().readBytes())
            gson.fromJson(jsonString, JsonFormat::class.java)
        } else {
            JsonFormat(mon, 0)
        }

        if (jsonFormat.week_beg == mon) return

        val writer: (FileOutputStream) -> Unit = { fos: FileOutputStream ->
            fos.write(gson.toJson(JsonFormat(mon, 0)).toByteArray())
        }
        fileStorageUtility.saveFile(
            parentDir, jsonFilename,
            writer
        )
    }

}