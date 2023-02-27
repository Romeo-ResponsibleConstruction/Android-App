package com.example.rc_app.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rc_app.data.repository.GalleryRepository

class ReceiptsViewModel(dataSource: GalleryRepository) : ViewModel() {
    val receiptsLiveData = dataSource.getReceiptList()

}

class ReceiptsViewModelFactory(param: GalleryRepository) :
    ViewModelProvider.Factory {
    private val dataSource: GalleryRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReceiptsViewModel(dataSource) as T
    }

    init {
        dataSource = param
    }
}