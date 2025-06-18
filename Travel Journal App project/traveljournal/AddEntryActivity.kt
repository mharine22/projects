package com.example.traveljournal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.traveljournal.model.Entry
import com.example.traveljournal.viewmodel.EntryViewModel
import java.io.File
import java.io.FileOutputStream

class AddEntryActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    private val entryViewModel: EntryViewModel by viewModels()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        titleEditText = findViewById(R.id.titleEditText)
        dateEditText = findViewById(R.id.dateEditText)
        contentEditText = findViewById(R.id.contentEditText)
        imageView = findViewById(R.id.imageView)

        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val saveButton = findViewById<Button>(R.id.saveButton)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            saveEntry()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val pickedUri = data?.data
            if (pickedUri != null) {
                selectedImageUri = getImageUriCopyToInternal(pickedUri)
                imageView.setImageURI(selectedImageUri)

                Log.d("AddEntryActivity", "Copied Local URI: $selectedImageUri")
                Toast.makeText(this, "Image selected successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEntry() {
        val title = titleEditText.text.toString()
        val date = dateEditText.text.toString()
        val content = contentEditText.text.toString()
        val imageUri = selectedImageUri?.toString()

        val newEntry = Entry(
            title = title,
            date = date,
            content = content,
            imageUri = imageUri
        )

        entryViewModel.insert(newEntry)

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getImageUriCopyToInternal(uri: Uri): Uri? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
