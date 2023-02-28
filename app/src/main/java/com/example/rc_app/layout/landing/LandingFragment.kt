package com.example.rc_app.layout.landing

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.data.viewModels.ReceiptsViewModel
import com.example.rc_app.data.viewModels.ReceiptsViewModelFactory
import com.example.rc_app.layout.header.CameraHeaderAdapter
import com.example.rc_app.layout.receiptLog.ReceiptLogAdapter
import com.example.rc_app.service.ReceiptService
import java.io.File

private const val FILE_NAME = "photo"
private const val REQUEST_CODE = 99

class LandingFragment : Fragment() {
    lateinit var dataSource: GalleryRepository
    lateinit var photoFile: File
    lateinit var receiptService: ReceiptService
    private val viewModel: ReceiptsViewModel by activityViewModels{ ReceiptsViewModelFactory(dataSource) }
    lateinit var headerAdapter: CameraHeaderAdapter
    private val receiptLogAdapter: ReceiptLogAdapter = ReceiptLogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = GalleryRepository(requireContext())
        receiptService = ReceiptService(requireContext(), dataSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        photoFile = getPhotoFile(FILE_NAME)

        headerAdapter = CameraHeaderAdapter(this, photoFile)
        val concatAdapter = ConcatAdapter(headerAdapter, receiptLogAdapter)

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.gallery_recycler_view)
        val manager = LinearLayoutManager(context);
        recyclerView.layoutManager = manager
        recyclerView.adapter = concatAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.receiptsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    receiptLogAdapter.submitList(it as MutableList<Receipt>)
                    headerAdapter.updateWeekCount(it.size)
                    headerAdapter.updatePendingCount(it.size)
                }
            }

        }
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
            receiptService.initiateSend()
            Toast.makeText(context, "Photo successfully saved! (hopefully)", Toast.LENGTH_SHORT).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}