package com.nikitamaslov.textlytic.picker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nikitamaslov.textlytic.R
import kotlinx.android.synthetic.main.activity_picker.*
import kotlinx.android.synthetic.main.dialog_picker.view.*

class PickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)

        pick_photo_btn.setOnClickListener {
            createPickerDialog().show()
        }
    }

    private fun createPickerDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_picker, null)
        dialog.setContentView(view)

        val takePictureItem = view.take_picture_item
        val chooseFromGalleryItem = view.choose_from_gallery_item

        takePictureItem.setOnClickListener {
            takePicture()
            dialog.dismiss()
        }
        chooseFromGalleryItem.setOnClickListener {
            chooseFromGallery()
            dialog.dismiss()
        }

        return dialog
    }

    private fun takePicture() {
        //stub
        Toast.makeText(this, "TAKING PICTURE", Toast.LENGTH_SHORT).show()
    }

    private fun chooseFromGallery() {
        //stub
        Toast.makeText(this, "CHOOSING FROM GALLERY", Toast.LENGTH_SHORT).show()
    }
}
