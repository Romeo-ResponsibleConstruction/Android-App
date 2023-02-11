package com.example.rc_app

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rc_app.databinding.ActivityMainBinding
import com.example.rc_app.entity.receipt.Receipt
import com.example.rc_app.gallery.GalleryRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var galleryRepo: GalleryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        galleryRepo = GalleryRepository(this.applicationContext)

        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val testRecpt = Receipt(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.headshot)
            )
            galleryRepo.saveToInternalStorage(testRecpt)
            Log.d("test-123", "hello-world")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



}