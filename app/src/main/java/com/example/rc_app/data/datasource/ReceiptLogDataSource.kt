package com.example.rc_app.data.datasource

import android.content.Context
import com.example.rc_app.entity.receipt.Receipt
import com.google.gson.Gson

class ReceiptLogDataSource(val context: Context) : DataSource<Receipt> {

    private val parentDir = "receiptLogDir"
    private val jsonFile = "log.json"
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
        var gson = Gson()
        val file = fileStorageUtility.getFile(parentDir, jsonFile)
        var jsonString = gson.fromJson(file.inputStream().readBytes().toString(), JsonFormat::class.java)
        jsonString.receipts.add(entity)
//        return fileStorageUtility.saveFile(parentDir, jsonFile, )
        TODO("finish impl")
    }

    override fun read(filepath: String): Receipt {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Receipt): Boolean {
        TODO("Not yet implemented")
    }


}