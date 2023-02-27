package com.example.rc_app.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.data.repository.GalleryRepository

class ReceiptService(val context: Context, val dataSource: GalleryRepository) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                val receiptList = dataSource.getReceiptList().value
                if (!receiptList.isNullOrEmpty()) {
                    val deque = ArrayDeque(receiptList)
                    sendToBucket(deque.last())
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
                super.onLost(network)
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun sendToBucket(receipt: Receipt): Boolean {
        // todo: call api here
        val success = true
//        Log.d("api", "sending")
//
//        if (success) {
//            dataSource.removeReceipt(receipt)
//            Log.d("api", "sent!")
//
//        } else {
//            Log.e("api", "failed to upload receipt")
//        }
        return success
    }
}