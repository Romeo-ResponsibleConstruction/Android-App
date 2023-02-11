package com.example.rc_app.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rc_app.R
import java.io.File

class GalleryFragment : Fragment() {

    private fun loadImages(pathname: String) {
//        TODO("todo")
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