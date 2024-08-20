package com.example.noted.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noted.model.Note

@Dao
interface NoteDao {
    //OnConflictStrategy means if the same primary key is present in the data then REPLACE the old data with new data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNode(note: Note)

    @Delete
    suspend fun deleteNode(note: Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    //  WHERE noteTitle LIKE :query OR noteDesc LIKE :query
//       Filters the results to include:
//       *   only those notes where the noteTitle or noteDesc contains the search term specified
//       *   by the 'query' parameter.
//       *   'query' is taken from searchNote(query
    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE :query OR noteDesc LIKE :query")
    fun searchNote(query: String?): LiveData<List<Note>>
}