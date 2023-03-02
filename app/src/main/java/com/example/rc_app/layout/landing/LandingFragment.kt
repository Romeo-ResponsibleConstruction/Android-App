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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rc_app.R
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.data.viewModels.ReceiptsViewModel
import com.example.rc_app.data.viewModels.ReceiptsViewModelFactory
import com.example.rc_app.layout.header.CameraHeaderAdapter
import com.example.rc_app.service.ReceiptService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

private const val FILE_NAME = "photo"
private const val CAMERA_CODE = 99

class LandingFragment : Fragment() {
    private lateinit var galleryRepository: GalleryRepository
    private lateinit var photoFile: File
    private val viewModel: ReceiptsViewModel by activityViewModels {
        ReceiptsViewModelFactory(
            galleryRepository
        )
    }
    lateinit var headerAdapter: CameraHeaderAdapter

    //    private val receiptLogAdapter: ReceiptLogAdapter = ReceiptLogAdapter()
    private lateinit var receiptService: ReceiptService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryRepository = GalleryRepository(requireContext())
        receiptService = ReceiptService(requireContext(), galleryRepository)
        galleryRepository.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        photoFile = getPhotoFile(FILE_NAME)

        headerAdapter = CameraHeaderAdapter(this, photoFile)
//        val concatAdapter = ConcatAdapter(headerAdapter, receiptLogAdapter)

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.gallery_recycler_view)
        val manager = LinearLayoutManager(context);
        recyclerView.layoutManager = manager
        recyclerView.adapter = headerAdapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.receiptsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    var size = it.size
                    if (receiptService.initiateSend()) {
                        size = 0
                    }
//                    receiptLogAdapter.submitList(it as MutableList<Receipt>)
                    headerAdapter.updatePendingCount(size)
                }

            }
        }

        viewModel.viewLiveData.observe(viewLifecycleOwner) {
            it?.let {
                headerAdapter.updateWeekCount(it)

            }
        }
    }

    private fun adapterOnClick(receipt: Receipt) {

    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private suspend fun addReceipt(receipt: Receipt) {
        return withContext(Dispatchers.IO) {
            viewModel.addReceipt(receipt)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            val generatedReceipt = Receipt(data?.extras?.get("data") as Bitmap)

            runBlocking {
                Toast.makeText(context, "Saving...", Toast.LENGTH_SHORT).show()

                launch {
                    addReceipt(generatedReceipt)
                    Toast.makeText(context, "Photo successfully saved!", Toast.LENGTH_SHORT).show()

                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}