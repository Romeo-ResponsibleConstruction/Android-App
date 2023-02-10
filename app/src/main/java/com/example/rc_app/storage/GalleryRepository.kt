package com.example.rc_app.storage

import android.content.Context
import com.example.rc_app.entity.Receipt

class GalleryRepository(var context: Context) : InternalRepository<Receipt> {
    override fun add(filetype: Receipt) {
//        context.openFileOutput(filetype)

    }

    override fun get(filename: String) {
        TODO("Not yet implemented")
    }

    override fun delete(filename: String) {
        TODO("Not yet implemented")
    }
}