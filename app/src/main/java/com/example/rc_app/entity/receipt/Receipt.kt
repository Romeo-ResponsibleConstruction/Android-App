package com.example.rc_app.entity.receipt
import android.graphics.Bitmap
import java.util.*

class Receipt (
    val image: Bitmap,
    val datetime: GregorianCalendar = GregorianCalendar(),
    val id: UUID = UUID.randomUUID()
) {

}
