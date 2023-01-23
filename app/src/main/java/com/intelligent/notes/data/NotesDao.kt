package com.intelligent.notes.data

import androidx.room.*
import com.intelligent.notes.data.models.Note
import kotlinx.coroutines.flow.Flow

// Data Access Object - where you define your database interactions.
@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE from todo_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * from todo_table WHERE id = :noteId")
    fun getSelectedNote(noteId: Int): Flow<Note>

    @Query("SELECT * from todo_table ORDER BY id ASC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery:String):Flow<List<Note>>

}