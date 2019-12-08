package com.nikitamaslov.textlytic.recognition

import android.net.Uri
import com.nikitamaslov.textlytic.util.Response

class FakeRecognizer : Recognizer {

    override fun recognize(imageUri: Uri, callback: (Response<String>) -> Unit) {
        val data =
            """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
                Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"""
        val response: Response<String> = Response.Success(data)
        callback(response)
    }
}
