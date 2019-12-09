package com.nikitamaslov.textlytic.recognition

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.nikitamaslov.textlytic.R
import com.nikitamaslov.textlytic.util.Response
import kotlinx.android.synthetic.main.content_recognition.*
import kotlinx.android.synthetic.main.header_recognition.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.nikitamaslov.textlytic.di.ServiceLocator
import java.io.IOException
import java.io.OutputStreamWriter


private const val CLIPBOARD_LABEL = "textlytic"
private const val TEXT_FILE_NAME = "textlytic.txt"

class RecognitionActivity : AppCompatActivity() {

    companion object {
        const val KEY_IMAGE_URI = "image-uri"
    }

    private lateinit var imageUri: Uri
    private lateinit var recognizer: Recognizer
    private var recognized: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initActionBar()
        initImageUri()

        recognized_image_view.setImageURI(imageUri)
        setListeners()

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

    private fun setListeners() {
        copy_to_clipboard_btn.setOnClickListener { copyToClipboard() }
        save_to_file_btn.setOnClickListener { saveToFile() }
    }

    private fun initDependencies() {
        ServiceLocator.init(this)
        this.recognizer = ServiceLocator.getRecognizer()
    }

    private fun recognize() {
        val callback: (Response<String>) -> Unit = {
            recognized_et.text = when (it) {
                is Response.Success -> it.content.also { str -> this.recognized = str }
                is Response.Failure -> getString(R.string.error_recognition)
            }
        }
        recognizer.recognize(imageUri, callback)
    }

    /*
     * ACTION region
     */

    private fun copyToClipboard() {
        if (recognized == null) return
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", recognized)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, R.string.notification_copied_to_clipboard, Toast.LENGTH_LONG).show()
    }

    private fun saveToFile() {
        if (recognized == null) {
            return
        }
        try {
            val outputStreamWriter =
                OutputStreamWriter(openFileOutput(TEXT_FILE_NAME, Context.MODE_PRIVATE))
            outputStreamWriter.write(recognized!!)
            outputStreamWriter.close()
            Toast.makeText(this, R.string.notification_saved_to_file, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, R.string.error_save_to_file, Toast.LENGTH_LONG).show()
        }

    }

    /*
     * end of region
     */
}
