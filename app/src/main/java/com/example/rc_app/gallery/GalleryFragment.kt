package com.example.rc_app.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rc_app.R
import com.example.rc_app.entity.receipt.Receipt
import java.util.*

class GalleryFragment : Fragment() {
    private var buffer: Queue<Receipt> = ArrayDeque()
    private lateinit var repo: GalleryRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repo = GalleryRepository(requireContext())
        buffer = ArrayDeque(repo.getAllFromStorage())
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }



}