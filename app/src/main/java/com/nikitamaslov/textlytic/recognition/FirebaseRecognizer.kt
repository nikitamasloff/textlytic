package com.nikitamaslov.textlytic.recognition

import android.content.Context
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.nikitamaslov.textlytic.util.Response

class FirebaseRecognizer(
    private val applicationContext: Context,
    private val vision: FirebaseVision
) : Recognizer {

    override fun recognize(
        imageUri: Uri,
        callback: (Response<String>) -> Unit
    ) {
        val image = FirebaseVisionImage.fromFilePath(applicationContext, imageUri)
        val detector = vision.onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener {
                val response = Response.Success(content = it.text)
                callback(response)
            }
            .addOnFailureListener {
                val response = Response.Failure(cause = it)
                callback(response)
            }
    }
}
