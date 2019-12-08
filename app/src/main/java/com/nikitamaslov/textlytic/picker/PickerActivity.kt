package com.nikitamaslov.textlytic.picker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nikitamaslov.textlytic.R
import kotlinx.android.synthetic.main.activity_picker.*
import kotlinx.android.synthetic.main.dialog_picker.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore.Images.Media

private const val REQUEST_CODE_TAKE_PHOTO = 1
private const val REQUEST_CODE_CHOOSE_FROM_GALLERY = 2

class PickerActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)

        pick_photo_btn.setOnClickListener {
            createPickerDialog().show()
        }
    }

    /*
     * DIALOG region
     */

    private fun createPickerDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_picker, null)
        dialog.setContentView(view)

        val takePictureItem = view.take_picture_item
        val chooseFromGalleryItem = view.choose_from_gallery_item

        val hasCamera = hasCamera()
        if (!hasCamera) {
            val textView = takePictureItem.take_picture_tv
            // strike thru a text
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        takePictureItem.setOnClickListener {
            takePicture()
            dialog.dismiss()
        }
        chooseFromGalleryItem.setOnClickListener {
            if (hasCamera) {
                chooseFromGallery()
                dialog.dismiss()
            } else {
                notifyNoCamera()
            }
        }

        return dialog
    }

    /*
     * end of region
     */


    /*
     * IMAGE CAPTURING region
     */

    private fun takePicture() {
        val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // check whether this intent can be handled by some app
        if (imageCaptureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            val photoFile: File = try {
                createImageFile()
            } catch (e: IOException) {
                // error occurred while creating the File
                // continue only if the File was successfully created
                return
            }

            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.nikitamaslov.textlytic.fileprovider",
                photoFile
            )
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(imageCaptureIntent, REQUEST_CODE_TAKE_PHOTO)
        }
    }

    private fun chooseFromGallery() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserTitle = getString(R.string.action_choose_from_gallery)
        val chooserIntent = Intent.createChooser(getIntent, chooserTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickIntent)

        // check whether this intent can be handled by some app
        if (chooserIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(chooserIntent, REQUEST_CODE_CHOOSE_FROM_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val imageUri: Uri = when (requestCode) {
            REQUEST_CODE_TAKE_PHOTO -> this.imageUri!!
            REQUEST_CODE_CHOOSE_FROM_GALLERY -> data!!.data!!
            else -> throw IllegalStateException("Unknown request code")
        }
        
        // TODO: send image uri to next activity
        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
    }

    /*
     * end of region
     */


    /*
     * NOTIFICATIONS region
     */

    private fun notifyNoCamera() {
        Toast.makeText(this, R.string.notification_camera_absent, Toast.LENGTH_LONG).show()
    }

    /*
     * end of region
     */


    /*
     * UTILITIES region
     */

    private fun hasCamera() = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    private fun createImageFile(): File {
        // Create an image file name
        val format = SimpleDateFormat("yyyy-MM-dd--HH:mm:ss", Locale.getDefault())
        val timeStamp = format.format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val tempFile = File.createTempFile(
            "textlytic#$timeStamp",   /* prefix */
            ".jpg",                   /* suffix */
            storageDir                      /* directory */
        )
        this.imageUri = Uri.fromFile(tempFile)
        return tempFile
    }

    /*
     * end of region
     */
}
