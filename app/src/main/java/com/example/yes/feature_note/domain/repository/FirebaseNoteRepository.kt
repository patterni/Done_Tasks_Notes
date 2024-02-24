package com.example.yes.feature_note.domain.repository

import com.example.yes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface FirebaseNoteRepository {

    fun getNotes(userId: String?): Flow<List<Note>>

    suspend fun getNoteById(userId: String?, noteId: String): Note?

    suspend fun insertNote(userId: String?, note: Note)

    suspend fun deleteNote(userId: String?, note: Note)
}