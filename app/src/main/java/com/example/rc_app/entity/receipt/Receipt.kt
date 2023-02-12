package com.example.rc_app.entity.receipt
import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*

class Receipt (
    val image: Bitmap,
    val calendar: GregorianCalendar = GregorianCalendar(),
    val id: UUID = UUID.randomUUID()
) {
    fun datetimeToString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(calendar.time)
    }

    fun idToString(): String {
        return id.toString()
    }
}
