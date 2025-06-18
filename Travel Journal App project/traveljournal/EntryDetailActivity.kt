package com.example.traveljournal

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.traveljournal.databinding.ActivityEntryDetailBinding

class EntryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val content = intent.getStringExtra("content")
        val imageUri = intent.getStringExtra("imageUri")

        Log.d("EntryDetailActivity", "imageUri: $imageUri")
        Toast.makeText(this, "Image URI: $imageUri", Toast.LENGTH_LONG).show()

        binding.detailTitle.text = title
        binding.detailDate.text = date
        binding.detailContent.text = content

        if (!imageUri.isNullOrEmpty()) {
            val uri = Uri.parse(imageUri)
            Glide.with(this)
                .load(uri)
                .error(R.drawable.error_image) // fallback if image loading fails
                .into(binding.detailImage)
        } else {
            binding.detailImage.setImageResource(R.drawable.error_image)
        }
    }
}
