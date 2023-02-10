package com.example.rc_app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.File

class GalleryFragment : Fragment() {

    private fun loadImages(pathname: String) {
        File(pathname).walk().forEach {
            Log.d("temp","$it")
            Log.d("temp","hello world")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadImages(".")
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }



}