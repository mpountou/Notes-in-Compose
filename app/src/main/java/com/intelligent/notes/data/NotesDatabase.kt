package com.intelligent.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.intelligent.notes.data.models.Note

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}