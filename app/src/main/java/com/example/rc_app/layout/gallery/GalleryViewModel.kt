package com.example.rc_app.layout.gallery

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt

class GalleryViewModel(dataSource: GalleryRepository) : ViewModel() {
    val receiptsLiveData = dataSource.getReceiptList()


}

class GalleryViewModelFactory(param: GalleryRepository) :
    ViewModelProvider.Factory {
    private val dataSource: GalleryRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(dataSource) as T
    }

    init {
        dataSource = param
    }
}