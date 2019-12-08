package com.nikitamaslov.textlytic.preview

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nikitamaslov.textlytic.R
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {

    companion object {
        const val KEY_IMAGE_URI = "image-uri"
    }

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val rawImageUri = intent.getStringExtra(KEY_IMAGE_URI)
            ?: throw IllegalStateException("No image URI found in passed intent")
        this.imageUri = Uri.parse(rawImageUri)

        preview_image_view.setImageURI(imageUri)
        recognize_btn.setOnClickListener {
            // TODO: launch recognition activity
        }
    }
}
