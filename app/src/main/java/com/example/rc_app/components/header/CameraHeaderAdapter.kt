package com.example.rc_app.components.header

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt

class CameraHeaderAdapter: RecyclerView.Adapter<CameraHeaderAdapter.HeaderViewHolder>() {

    private var latestReceipt: Receipt? = null

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val latestPhotoView: ImageView = itemView.findViewById(R.id.imageView3)

        fun bind(latestReceipt: Receipt?) {
            latestPhotoView.setImageBitmap(latestReceipt?.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(latestReceipt)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun updateLatestImage(updatedImage: Receipt) {
        latestReceipt = updatedImage
        notifyDataSetChanged()
    }

}