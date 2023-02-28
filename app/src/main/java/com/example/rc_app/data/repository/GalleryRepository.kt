package com.example.rc_app.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.data.datasource.ReceiptImageDataSource

class GalleryRepository(val context: Context) {
    private val repo: ReceiptImageDataSource = ReceiptImageDataSource(context)
    private val bufferLiveData = MutableLiveData(repo.getAllFromStorage())

    fun addReceipt(receipt: Receipt) {
        repo.save(receipt)
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
            repo.delete(receipt)
            val updatedList = currentList.toMutableList()
            updatedList.remove(receipt)
            bufferLiveData.postValue(updatedList)
            Toast.makeText(context, "Sent to api", Toast.LENGTH_SHORT).show()
        }
    }

}