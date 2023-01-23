package com.intelligent.notes.data.repositories

import com.intelligent.notes.data.NotesDao
import com.intelligent.notes.data.models.Note
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class NotesRepository @Inject constructor(private val notesDao: NotesDao) {

    val getAllNotes: Flow<List<Note>> = notesDao.getAllNotes()

    fun getSelectedNote(noteId: Int): Flow<Note> {
        return notesDao.getSelectedNote(noteId = noteId)
    }

    suspend fun addNote(note: Note) {
        notesDao.addNote(note = note)
    }

    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note = note)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note = note)
    }

    suspend fun deleteAllNotes() {
        notesDao.deleteAllNotes()
    }

    fun searchDatabase(searchQuery: String): Flow<List<Note>> {
        return notesDao.searchDatabase(searchQuery = searchQuery)
    }
}