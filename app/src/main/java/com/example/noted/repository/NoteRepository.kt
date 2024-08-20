package com.example.noted.repository


import com.example.noted.database.NoteDatabase
import com.example.noted.model.Note

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note)= db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note)= db.getNoteDao().deleteNode(note)
    suspend fun updateNote(note: Note)= db.getNoteDao().updateNode(note)

    fun getAllNotes()=db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)
}