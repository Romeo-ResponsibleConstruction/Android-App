package com.example.rc_app.components.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.components.header.CameraHeaderAdapter
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
        val headerAdapter = CameraHeaderAdapter()
        val receiptsAdapter = ReceiptAdapter { receipt: Receipt -> adapterOnClick(receipt) }
        val concatAdapter = ConcatAdapter(headerAdapter, receiptsAdapter)

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.gallery_recycler_view)
        val manager = GridLayoutManager(context, 2);
        recyclerView.layoutManager = manager

        manager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) manager.spanCount else 1
            }
        }

        recyclerView.adapter = concatAdapter

        dataSource.getReceiptList().observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    receiptsAdapter.submitList(it as MutableList<Receipt>)
                    headerAdapter.updateLatestImage(it[0])
                }
            }

        }

        return view
    }

    private fun adapterOnClick(receipt: Receipt) {

    }

}