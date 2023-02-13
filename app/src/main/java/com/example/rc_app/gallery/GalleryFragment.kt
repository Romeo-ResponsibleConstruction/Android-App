package com.example.rc_app.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.entity.receipt.receiptList.ReceiptAdapter

class GalleryFragment : Fragment() {

    lateinit var dataSource: GalleryDataSource


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataSource = GalleryDataSource(requireActivity())
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        if (view is RecyclerView) {
            val recyclerView: RecyclerView = view
            val receiptAdapter = ReceiptAdapter { receipt: Receipt -> adapterOnClick(receipt) }
            recyclerView.adapter = receiptAdapter
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.isNestedScrollingEnabled = false

            dataSource.getReceiptList().observe(viewLifecycleOwner) {
                it?.let {
                    if (!it.isEmpty()) {
                        receiptAdapter.submitList(it as MutableList<Receipt>)
                    }
                }
            }

        }

        return view
    }

    private fun adapterOnClick(receipt: Receipt) {

    }

}