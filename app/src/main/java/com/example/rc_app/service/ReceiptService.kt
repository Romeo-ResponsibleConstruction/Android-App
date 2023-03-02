package com.example.rc_app.service

import android.content.Context
import android.graphics.Bitmap
import android.net.*
import android.widget.Toast
import androidx.core.net.toUri
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
import java.util.*
import kotlin.collections.HashSet

class ReceiptService(val context: Context, val galleryRepository: GalleryRepository) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private var isConnected = false
    private var inFlightTasks: HashSet<StorageTask<UploadTask.TaskSnapshot>> = HashSet()
    private var retryTasks: Queue<Pair<Receipt, Uri?>> = LinkedList()

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
                initiateSend()
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

    fun initiateSend(): Boolean {
        val hasSent = false
        val receiptQueue: Queue<Receipt> =
            LinkedList(galleryRepository.getReceiptList().value ?: emptyList())
        while (isConnected && !receiptQueue.isEmpty()) {
            while (!retryTasks.isEmpty()) {
                val task = retryTasks.poll()
                val uploadTask = sendToBucket(task.first, task.second)
                inFlightTasks.add(uploadTask)
            }
            while (!receiptQueue.isEmpty()) {
                val receipt = receiptQueue.poll()
                val uploadTask = sendToBucket(receipt)
                inFlightTasks.add(uploadTask)
            }
        }
        return hasSent
    }

    private fun sendToBucket(
        receipt: Receipt,
        uploadUri: Uri? = null
    ): StorageTask<UploadTask.TaskSnapshot> {
        // todo: call api here

        val file = galleryRepository.getFileFromReceipt(receipt)
        val uri = file.toUri()

        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }

        var progress = 0.0


        val uploadTask = if (uploadUri != null) {
            storageRef.child(receipt.idToString()).putFile(uri, metadata, uploadUri)
        } else {
            storageRef.child(receipt.idToString()).putFile(uri, metadata)
        }
        println(storageRef.path)

        uploadTask.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
            run {
                Toast.makeText(context, "Upload Succeeded", Toast.LENGTH_SHORT).show()
                inFlightTasks.remove(uploadTask)
                galleryRepository.removeReceipt(receipt)
            }
        }
            .addOnFailureListener { exception: java.lang.Exception ->
                run {
                    Toast.makeText(context, "Failed: " + exception.message, Toast.LENGTH_LONG)
                        .show()
                    val uploadSessionURI = uploadTask.snapshot.uploadSessionUri
                    retryTasks.add(Pair(receipt, uploadSessionURI))
                    inFlightTasks.remove(uploadTask)
                }
            }
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                run {
                    println(100.0 * (taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount))
                }
            }

        return uploadTask
    }
}