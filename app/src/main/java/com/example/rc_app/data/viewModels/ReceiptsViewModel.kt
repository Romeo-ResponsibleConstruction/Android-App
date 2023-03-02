package com.example.rc_app.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt
import java.io.File

class ReceiptsViewModel(private val galleryRepository: GalleryRepository) : ViewModel() {

    val receiptsLiveData = galleryRepository.getReceiptList()
    val viewLiveData = galleryRepository.getTotalList()

    fun addReceipt(receipt: Receipt) {
        galleryRepository.addReceipt(receipt)
    }

    fun removeReceipt(receipt: Receipt) {
        galleryRepository.removeReceipt(receipt)
    }

    fun getFileFromReceipt(receipt: Receipt): File {
        return galleryRepository.getFileFromReceipt(receipt)
    }

}

class ReceiptsViewModelFactory(param: GalleryRepository) :
    ViewModelProvider.Factory {
    private val galleryRepository: GalleryRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReceiptsViewModel(galleryRepository) as T
    }

    init {
        galleryRepository = param
    }
}