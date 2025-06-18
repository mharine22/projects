package com.example.traveljournal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.traveljournal.db.EntryDatabase
import com.example.traveljournal.model.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val entryDao = EntryDatabase.getDatabase(application).entryDao()
    val allEntries: LiveData<List<Entry>> = entryDao.getAllEntries()

    fun insert(entry: Entry) {
        viewModelScope.launch(Dispatchers.IO) {
            entryDao.insert(entry)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            entryDao.deleteAllEntries()
        }
    }

    // New method to delete a specific entry by its ID
    fun deleteEntryById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            entryDao.deleteById(id)
        }
    }
}
