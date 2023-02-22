package com.example.rc_app.layout.receiptLog

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt

class ReceiptLogItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val receiptDateTaken: TextView = itemView.findViewById(R.id.dateTakenView)
    private val receiptUUID: TextView = itemView.findViewById(R.id.uuidView)

    private var currentReceipt: Receipt? = null

    fun bind(receipt: Receipt) {
        currentReceipt = receipt
        receiptDateTaken.text = receipt.datetimeToString()
        receiptUUID.text = receipt.id.toString()
    }
}