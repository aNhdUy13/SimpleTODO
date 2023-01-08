package com.nda.simpletodo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nda.simpletodo.models.Note

@Dao
interface NoteDAO {

    @Query("SELECT * FROM noteTable")
    fun getAllNote(): List<Note>

    @Insert
    fun insertNote(note:Note)

    @Update
    fun updateNote(note:Note)

    @Delete
    fun deleteNote(note: Note)
}