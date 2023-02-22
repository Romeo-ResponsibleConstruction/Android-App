package com.example.rc_app.layout.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.layout.gallery.GalleryItemViewHolder

class ReceiptsAdapter(private val onClick: (Receipt) -> Unit) :
    ListAdapter<Receipt, GalleryItemViewHolder>(ReceiptDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_item, parent, false)
        return GalleryItemViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        val receipt = getItem(position)
        holder.bind(receipt)
    }
}

object ReceiptDiffCallback : DiffUtil.ItemCallback<Receipt>() {
    override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem.id == newItem.id
    }
}