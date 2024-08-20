package com.example.noted.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noted.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        //volatile means changes made in 1 thread are IMMEDIATELY visible to other thread
        @Volatile
        private var instance: NoteDatabase? = null

        //LOCK is used to sync the creation of database i.e. only 1 thread can execute the code inside the block at a time
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"
            ).build()
    }
}