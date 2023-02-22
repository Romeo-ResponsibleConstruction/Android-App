package com.example.rc_app.layout.receiptLog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.layout.receipt.ReceiptDiffCallback

class ReceiptLogAdapter :
    ListAdapter<Receipt, ReceiptLogItemViewHolder>(ReceiptDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptLogItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.receipt_log_item, parent, false)
        return ReceiptLogItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptLogItemViewHolder, position: Int) {
        val receipt = getItem(position)
        holder.bind(receipt)
    }
}