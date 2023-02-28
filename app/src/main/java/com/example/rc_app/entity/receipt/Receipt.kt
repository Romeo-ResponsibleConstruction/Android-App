package com.example.rc_app.entity.receipt
import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*

class Receipt (
    val image: Bitmap?,
    val status: ReceiptStatus = ReceiptStatus.PENDING,
    val dateCreated: GregorianCalendar = GregorianCalendar(),
    val id: UUID = UUID.randomUUID()
) {
    fun datetimeToString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(dateCreated.time)
    }

    fun idToString(): String {
        return id.toString()
    }

}
