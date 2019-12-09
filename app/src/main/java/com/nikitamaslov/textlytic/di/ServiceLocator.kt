package com.nikitamaslov.textlytic.di

import android.content.Context
import com.google.firebase.ml.vision.FirebaseVision
import com.nikitamaslov.textlytic.recognition.FirebaseRecognizer
import com.nikitamaslov.textlytic.recognition.Recognizer

object ServiceLocator {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        this.applicationContext = context.applicationContext
    }

    fun getRecognizer(): Recognizer = FirebaseRecognizer(
        applicationContext = applicationContext,
        vision = getFirebaseVision()
    )

    private fun getFirebaseVision(): FirebaseVision = FirebaseVision.getInstance()
}