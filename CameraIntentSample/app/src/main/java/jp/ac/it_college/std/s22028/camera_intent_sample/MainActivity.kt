package jp.ac.it_college.std.s22028.camera_intent_sample

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import jp.ac.it_college.std.s22028.camera_intent_sample.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private  var imageUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result?.resultCode == RESULT_OK) {
            binding.ivCamera.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamera.setOnClickListener { onCameraImageClick() }
    }

    private fun onCameraImageClick() {
        val timestamp = SimpleDateFormat("yyyMMddHHmmss", Locale.ROOT).format(Date())
        val fileName = "Photo_${timestamp}.jpg"

        val values = ContentValues().apply {
            // ファイル名の指定
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            // ファイル形成の指定
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // 保存ディレクトリの指定
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraIntentSample")
        }

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        cameraLauncher.launch(intent)
    }
}