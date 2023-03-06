package com.example.rc_app.layout.header

import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import java.io.File


private const val FILE_NAME = "photo.png"
private const val CAMERA_CODE = 1231
private const val GALLERY_CODE = 1111

class CameraHeaderAdapter(val fragment: Fragment, val photoFile: File): RecyclerView.Adapter<CameraHeaderAdapter.HeaderViewHolder>(), View.OnClickListener {

    private var totalCount: Int = 0
    private var pendingCount: Int = 0

    lateinit var parent: ViewGroup

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val btnOpenCamera: Button = itemView.findViewById(R.id.btnOpenCamera)
        val totalTaken: TextView = itemView.findViewById(R.id.takenNumView)
        val pendingNum: TextView = itemView.findViewById(R.id.pendingNumView)
        val view: View = view

        fun bind(numTaken: Int, numPending: Int) {
            totalTaken.text = numTaken.toString()
            pendingNum.text = numPending.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(totalCount, pendingCount)
        holder.btnOpenCamera.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun updateWeekCount(updatedWeekCount: Int) {
        totalCount = updatedWeekCount
        notifyDataSetChanged()
    }

    fun updatePendingCount(updatedPendingCount: Int) {
        pendingCount = updatedPendingCount
        notifyDataSetChanged()
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.btnOpenCamera -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    val fileProvider = FileProvider.getUriForFile(fragment.requireContext(), "com.example.fileprovider", photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                    if (cameraIntent.resolveActivity(fragment.requireContext().packageManager) != null) {
                        fragment.startActivityForResult(cameraIntent, CAMERA_CODE)
                    } else {
                        Toast.makeText(fragment.requireContext(), "Unable to open camera.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.btnOpenGallery -> {
                    /*
                    val galleryIntent = Intent(Intent.ACTION_PICK)
                    galleryIntent.type = "image/*"

                    val fileProvider = FileProvider.getUriForFile(fragment.requireContext(), "com.example.fileprovider", photoFile)
                    galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                    fragment.startActivityForResult(galleryIntent, GALLERY_CODE)

                     */

                     */

                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE)
                    print("Works")

                }
                else -> {}
            }
        }

    }

}