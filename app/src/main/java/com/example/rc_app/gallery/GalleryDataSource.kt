package com.example.rc_app.gallery

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rc_app.entity.receipt.Receipt

class GalleryDataSource(context: Context) {
    private val repo: GalleryRepository = GalleryRepository(context)
    private val bufferLiveData = MutableLiveData(repo.getAllFromStorage())

    fun addReceipt(receipt: Receipt) {
        repo.saveToInternalStorage(receipt)
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
            val updatedList = currentList.toMutableList()
            updatedList.remove(receipt)
            bufferLiveData.postValue(updatedList)
        }
    }

}