package com.example.rc_app.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt

class ReceiptsViewModel(private val galleryRepository: GalleryRepository) : ViewModel() {

    val receiptsLiveData = galleryRepository.getReceiptList()

    fun addReceipt(receipt: Receipt) {
        galleryRepository.addReceipt(receipt)
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