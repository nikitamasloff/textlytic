package com.nikitamaslov.textlytic.recognition

import android.net.Uri
import com.nikitamaslov.textlytic.util.Response

interface Recognizer {

    fun recognize(
        imageUri: Uri,
        callback: (Response<String>) -> Unit
    )
}
