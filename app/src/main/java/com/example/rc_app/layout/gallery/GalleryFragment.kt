package com.example.rc_app.layout.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.data.viewModels.ReceiptsViewModel
import com.example.rc_app.data.viewModels.ReceiptsViewModelFactory
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.layout.receipt.ReceiptsAdapter


class GalleryFragment : Fragment() {

    lateinit var dataSource: GalleryRepository
    private val viewModel: ReceiptsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        return view
    }


    private fun adapterOnClick(receipt: Receipt) {

    }



}