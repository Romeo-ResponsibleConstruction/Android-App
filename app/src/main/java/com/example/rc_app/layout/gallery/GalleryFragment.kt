package com.example.rc_app.layout.gallery

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.layout.header.CameraHeaderAdapter
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.layout.receipt.ReceiptsAdapter
import com.example.rc_app.service.ReceiptService
import java.io.File



class GalleryFragment : Fragment() {

    lateinit var dataSource: GalleryRepository
    lateinit var receiptService: ReceiptService
    private val viewModel: GalleryViewModel by viewModels{ GalleryViewModelFactory(dataSource)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataSource = GalleryRepository(requireActivity())

        val receiptsAdapter = ReceiptsAdapter { receipt: Receipt -> adapterOnClick(receipt) }

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.gallery_recycler_view)
        val manager = GridLayoutManager(context, 2);
        recyclerView.layoutManager = manager
        recyclerView.adapter = receiptsAdapter

        viewModel.receiptsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    receiptsAdapter.submitList(it as MutableList<Receipt>)
                }
            }

        }

        receiptService = ReceiptService(requireContext(), dataSource)
        return view
    }


    private fun adapterOnClick(receipt: Receipt) {

    }



}