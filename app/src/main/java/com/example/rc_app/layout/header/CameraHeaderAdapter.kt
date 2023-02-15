package com.example.rc_app.components.header

import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 99

class CameraHeaderAdapter(val fragment: Fragment, val photoFile: File): RecyclerView.Adapter<CameraHeaderAdapter.HeaderViewHolder>(), View.OnClickListener {

    private var latestReceipt: Receipt? = null

    lateinit var parent: ViewGroup

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val latestPhotoView: ImageView = itemView.findViewById(R.id.imageView3)
        val btnOpenCamera: Button = itemView.findViewById(R.id.btnOpenCamera)
        val view: View = view

        fun bind(latestReceipt: Receipt?) {
            latestPhotoView.setImageBitmap(latestReceipt?.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(latestReceipt)
        holder.btnOpenCamera.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun updateLatestImage(updatedImage: Receipt) {
        latestReceipt = updatedImage
        notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val fileProvider = FileProvider.getUriForFile(fragment.requireContext(), "com.example.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(fragment.requireContext().packageManager) != null) {
            fragment.startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(fragment.requireContext(), "Unable to open camera.", Toast.LENGTH_SHORT).show()
        }
    }

}