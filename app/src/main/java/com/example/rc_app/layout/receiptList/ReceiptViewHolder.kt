package com.example.rc_app.layout.receiptList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt

class ReceiptViewHolder(itemView: View, val onClick: (Receipt) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val receiptTextView: TextView = itemView.findViewById(R.id.receiptMetadata)
    private val receiptImageView: ImageView = itemView.findViewById(R.id.receiptImage)

    private var currentReceipt: Receipt? = null

    init {
        itemView.setOnClickListener {
            currentReceipt?.let {
                onClick(it)
            }
        }
    }

    fun bind(receipt: Receipt) {
        currentReceipt = receipt
        receiptTextView.text = receipt.datetimeToString()
        receiptImageView.setImageBitmap(receipt.image)
    }
}
