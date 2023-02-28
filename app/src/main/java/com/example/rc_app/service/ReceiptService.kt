package com.example.rc_app.service

import android.content.Context
import android.graphics.Bitmap
import android.net.*
import android.widget.Toast
import com.example.rc_app.data.repository.GalleryRepository
import com.example.rc_app.entity.receipt.Receipt
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream

class ReceiptService(val context: Context, val galleryRepository: GalleryRepository) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private var isConnected = false

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
                val receiptList = galleryRepository.getReceiptList().value
                while (isConnected && !receiptList.isNullOrEmpty()) {
                    val deque = ArrayDeque(receiptList)
                    val receiptLast = deque.last()
                    if (sendToBucket(receiptLast)) {
                        galleryRepository.removeReceipt(receiptLast)
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

    private fun sendToBucket(receipt: Receipt): Boolean {
        // todo: call api here
        val bos = ByteArrayOutputStream()
        receipt.image.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitMapData = bos.toByteArray()

        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }

        var success = false
        var progress = 0.0

        storageRef.child("gpr_dummy/${receipt.idToString()}").putBytes(bitMapData, metadata)
            .addOnSuccessListener {
                fun successHandler(taskSnapshot: UploadTask.TaskSnapshot) {
                    Toast.makeText(context, "Upload Succeeded", Toast.LENGTH_SHORT).show()
                    success = true
                }
            }
            .addOnFailureListener {
                fun failureHandler(exception: java.lang.Exception) {
                    Toast.makeText(context, "Failed: " + exception.message, Toast.LENGTH_SHORT)
                        .show()
                    success = false
                }
            }
            .addOnProgressListener {
                fun progressHandler(taskSnapshot: UploadTask.TaskSnapshot) {
                    progress = 100.0 * (taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                }
            }

        return success
    }
}