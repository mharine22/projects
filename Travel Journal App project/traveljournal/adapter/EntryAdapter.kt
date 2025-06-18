package com.example.traveljournal.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.traveljournal.EntryDetailActivity
import com.example.traveljournal.databinding.ItemEntryBinding
import com.example.traveljournal.model.Entry
import com.example.traveljournal.viewmodel.EntryViewModel
import java.text.SimpleDateFormat
import java.util.*

class EntryAdapter(
    private var entryList: MutableList<Entry>,
    private val entryViewModel: EntryViewModel
) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {


    inner class EntryViewHolder(val binding: ItemEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entryList[position]
        val binding = holder.binding

        binding.entryTitle.text = entry.title
        binding.entryDate.text = entry.date ?: "No Date Available"
        binding.entryContent.text = entry.content

        // ✅ Log the URI to debug
        Log.d("EntryAdapter", "Image URI: ${entry.imageUri}")

        // ✅ Load image from actual imageUri
        if (!entry.imageUri.isNullOrEmpty()) {
            Glide.with(binding.root.context)
                .load(Uri.parse(entry.imageUri))
                .into(binding.entryImage)
        } else {
            // Clear image if there's no URI to avoid old images showing
            binding.entryImage.setImageDrawable(null)
        }

        // Open detail screen on click
        binding.root.setOnClickListener {
            val context = binding.root.context
            val intent = Intent(context, EntryDetailActivity::class.java).apply {
                putExtra("title", entry.title)
                putExtra("date", entry.date)
                putExtra("content", entry.content)
                putExtra("imageUri", entry.imageUri)
            }
            context.startActivity(intent)
        }

        // Delete entry on delete button click
        binding.entryDeleteButton.setOnClickListener {
            confirmDelete(position, binding.root.context)
        }
    }






    override fun getItemCount(): Int = entryList.size

    fun updateList(newList: List<Entry>) {
        entryList.clear()
        entryList.addAll(newList)
        notifyDataSetChanged()
    }

    private fun confirmDelete(position: Int, context: Context) {
        if (position >= 0 && position < entryList.size) {
            val entry = entryList[position]
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes") { _, _ ->
                    // Delete from DB using ViewModel
                    entry.id?.let { entryViewModel.deleteEntryById(it) }

                    // Remove from list and notify adapter
                    entryList.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
    //3.adb shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///storage/emulated/0/Pictures/tajmahal.jpg

    //1.cd C:\Users\mhari\AppData\Local\Android\Sdk\platform-tools

    //4.adb shell ls /sdcard/Pictures

    //2.adb push C:\Users\<YourUsername>\Downloads\myimage.jpg /sdcard/Pictures/


}
