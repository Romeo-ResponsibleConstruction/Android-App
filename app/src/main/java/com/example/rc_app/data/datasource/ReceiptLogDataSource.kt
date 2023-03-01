package com.example.rc_app.data.datasource

import android.content.Context
import com.example.rc_app.entity.receipt.Receipt
import com.google.api.client.json.Json
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream

class ReceiptLogDataSource(val context: Context) : DataSource<Receipt> {

    private val parentDir = "receiptLogDir"
    private val jsonFilename = "log.json"
    private val fileStorageUtility: FileStorageDataSource<Receipt> = InternalFileStorageDataSource(context)

    private class JsonFormat(val week_beg: String, val receipts: MutableList<Receipt>) {
        fun addReceipt(receipt: Receipt) {
            receipts.add(receipt)
        }
        fun removeReceipt(receipt: Receipt) {
            receipts.remove(receipt)
        }
    }

    override fun save(entity: Receipt): String {
//        create a gson object
        val gson = Gson()
        val initialFile = fileStorageUtility.getFile(parentDir, jsonFilename)
        var jsonFormat: JsonFormat
        if (initialFile.exists()) {
            jsonFormat = gson.fromJson(initialFile.inputStream().readBytes().toString(), JsonFormat::class.java)
            jsonFormat.receipts.add(entity)
        } else {
            jsonFormat = JsonFormat(entity.datetimeToString(), mutableListOf(entity))
        }

        val writer: (FileOutputStream) -> Unit = {fos:FileOutputStream ->
            fos.write(gson.toJson(jsonFormat).toByteArray())
        }
        return fileStorageUtility.saveFile(parentDir, jsonFilename,
            writer
        )
        return ""
    }

    override fun read(filepath: String): Receipt {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Receipt): Boolean {
        TODO("Not yet implemented")
    }

    fun onAppStart() {
        TODO("on app start, check if same week. if different week then filter out accepted receipts")

    }

}