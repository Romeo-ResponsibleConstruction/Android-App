package com.example.rc_app.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.data.datasource.ReceiptFileDataSource
import java.io.File

class GalleryRepository(val context: Context) {
    private val fileDataSource: ReceiptFileDataSource = ReceiptFileDataSource(context)
    private val bufferLiveData = MutableLiveData(fileDataSource.getAllFromStorage())

    fun getFileFromReceipt(receipt: Receipt): File {
        return repo.getFileFromReceipt(receipt)
    }

    fun addReceipt(receipt: Receipt) {
        fileDataSource.save(receipt)
        val currentList = bufferLiveData.value
        if (currentList == null) {
            bufferLiveData.postValue(listOf(receipt))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, receipt)
            bufferLiveData.postValue(updatedList)
        }
    }

    fun getReceiptList(): LiveData<List<Receipt>> {
        return bufferLiveData
    }

    fun removeReceipt(receipt: Receipt) {
        val currentList = bufferLiveData.value
        if (currentList != null) {
            fileDataSource.delete(receipt)
            val updatedList = currentList.toMutableList()
            updatedList.remove(receipt)
            bufferLiveData.postValue(updatedList)
            Toast.makeText(context, "Sent to api", Toast.LENGTH_SHORT).show()
        }
    }

}