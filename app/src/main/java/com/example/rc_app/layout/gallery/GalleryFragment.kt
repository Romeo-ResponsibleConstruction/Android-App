package com.example.rc_app.components.gallery

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.components.header.CameraHeaderAdapter
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.components.receiptList.ReceiptAdapter
import java.io.File


private const val FILE_NAME = "photo"
private const val REQUEST_CODE = 99


class GalleryFragment : Fragment() {

    lateinit var dataSource: GalleryDataSource
    lateinit var photoFile: File


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataSource = GalleryDataSource(requireActivity())
        photoFile = getPhotoFile(FILE_NAME)

        val headerAdapter = CameraHeaderAdapter(this, photoFile)
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

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565

            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath, options)
            val testRecpt = Receipt(takenImage)
            Toast.makeText(context, "Saving...", Toast.LENGTH_SHORT).show()
            dataSource.addReceipt(testRecpt)
            Toast.makeText(context, "Photo successfully saved! (hopefully)", Toast.LENGTH_SHORT).show()

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}