package com.example.rc_app.service

import android.content.Context
import android.graphics.Bitmap
import android.net.*
import android.widget.Toast
import androidx.core.net.toUri
import com.example.rc_app.data.datasource.ReceiptFileDataSource
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ReceiptService(val context: Context, val galleryRepository: GalleryRepository) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private var isConnected = false
    private var inFlightTasks: HashSet<StorageTask<UploadTask.TaskSnapshot>> = HashSet()
    private var retryTasks: HashSet<Pair<Receipt, Uri?>> = HashSet()

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                isConnected = true

                val receiptQueue = galleryRepository.getReceiptList().value
                while (isConnected && !receiptQueue.isNullOrEmpty()) {
                    for (task in retryTasks){
                        val uploadTask = sendToBucket(task.first, task.second)
                        inFlightTasks.add(uploadTask)
                    }
                    for (receipt in receiptQueue){
                        val uploadTask = sendToBucket(receipt)
                        inFlightTasks.add(uploadTask)
                    }
                }
                super.onAvailable(network)
            }

            // Network capabilities have changed for the network
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val unmetered =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            }

            // lost network connection
            override fun onLost(network: Network) {
                isConnected = false
                super.onLost(network)
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }


    private fun sendToBucket(receipt: Receipt, uploadUri: Uri? = null): StorageTask<UploadTask.TaskSnapshot> {
        // todo: call api here

        val file = galleryRepository.getFileFromReceipt(receipt)
        val uri = file.toUri()

        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }

        var progress = 0.0


        var uploadTask = if (uploadUri != null){
            storageRef.child("gpr_dummy/${receipt.idToString()}").putFile(uri, metadata, uploadUri)
        } else {
            storageRef.child("gpr_dummy/${receipt.idToString()}").putFile(uri, metadata)
        }

        uploadTask.addOnSuccessListener {
            fun successHandler(taskSnapshot: UploadTask.TaskSnapshot) {
                Toast.makeText(context, "Upload Succeeded", Toast.LENGTH_SHORT).show()
                inFlightTasks.remove(uploadTask)
                galleryRepository.removeReceipt(receipt)
            }
        }
        .addOnFailureListener {
            fun failureHandler(exception: java.lang.Exception) {
                Toast.makeText(context, "Failed: " + exception.message, Toast.LENGTH_SHORT)
                    .show()
                val uploadSessionURI = uploadTask.snapshot.uploadSessionUri
                retryTasks.add(Pair(receipt,uploadSessionURI))
                inFlightTasks.remove(uploadTask)
            }
        }
        .addOnProgressListener {
            fun progressHandler(taskSnapshot: UploadTask.TaskSnapshot) {
                println(100.0 * (taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount))
            }
        }

        return uploadTask
    }
}