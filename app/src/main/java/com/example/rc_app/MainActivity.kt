package com.example.rc_app

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.example.rc_app.databinding.ActivityMainBinding
import com.example.rc_app.components.gallery.GalleryDataSource
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 99
private lateinit var photoFile: File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var galleryDataSource: GalleryDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        galleryDataSource = GalleryDataSource(this.applicationContext)
//
//        val btnCamera: Button = findViewById(R.id.btnOpenCamera)
//        btnCamera.setOnClickListener {
//            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            photoFile = getPhotoFile(FILE_NAME)
//
//            // This doesn't work for API >= 24 (circa 2016)
//            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
//
//            val fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile)
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
//            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
//                startActivityForResult(takePictureIntent, REQUEST_CODE)
//            } else {
//                Toast.makeText(this, "Unable to open camera.", Toast.LENGTH_SHORT).show()
//            }
//
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun getPhotoFile(fileName: String): File {
        // Use 'getExternalFilesDir' on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
//            val testRecpt = Receipt(BitmapFactory.decodeFile(photoFile.absolutePath))
//            Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
//            galleryDataSource.addReceipt(testRecpt)
//            Toast.makeText(this, "Photo successfully saved! (hopefully)", Toast.LENGTH_SHORT).show()
//
//            val imageView = findViewById<ImageView>(R.id.imageView3)
//            imageView.setImageBitmap(takenImage)
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }



}