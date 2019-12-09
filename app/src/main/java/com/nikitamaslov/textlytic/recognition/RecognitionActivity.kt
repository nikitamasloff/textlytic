package com.nikitamaslov.textlytic.recognition

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.nikitamaslov.textlytic.R
import com.nikitamaslov.textlytic.util.Response
import kotlinx.android.synthetic.main.content_recognition.*
import kotlinx.android.synthetic.main.header_recognition.*

class RecognitionActivity : AppCompatActivity() {

    companion object {
        const val KEY_IMAGE_URI = "image-uri"
    }

    private lateinit var imageUri: Uri
    private lateinit var recognizer: Recognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initActionBar()
        initImageUri()

        recognized_image_view.setImageURI(imageUri)

        initDependencies()
        recognize()
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initImageUri() {
        val rawImageUri = intent.getStringExtra(KEY_IMAGE_URI)
            ?: throw IllegalStateException("No image URI found in passed intent")
        this.imageUri = Uri.parse(rawImageUri)
    }

    private fun initDependencies() {
        this.recognizer = FirebaseRecognizer(applicationContext, FirebaseVision.getInstance())
    }

    private fun recognize() {
        val callback: (Response<String>) -> Unit = {
            recognized_et.text = when (it) {
                is Response.Success -> it.content
                is Response.Failure -> "error"
            }
        }
        recognizer.recognize(imageUri, callback)
    }
}
