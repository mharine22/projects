package com.example.traveljournal.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.traveljournal.model.Entry

@Dao
interface EntryDao {

    @Insert
    suspend fun insert(entry: Entry)

    @Query("SELECT * FROM entries")
    fun getAllEntries(): LiveData<List<Entry>>

    @Query("DELETE FROM entries")
    suspend fun deleteAllEntries()

    // New query to delete an entry by its ID
    @Query("DELETE FROM entries WHERE id = :id")
    suspend fun deleteById(id: Long)

}
