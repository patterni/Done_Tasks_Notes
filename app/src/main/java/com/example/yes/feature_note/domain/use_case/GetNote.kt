package com.example.yes.feature_note.domain.use_case

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository

class GetNote (private val repository: FirebaseNoteRepository) {
    suspend operator fun invoke(userId: String?, id: String): Note? {
        return repository.getNoteById(userId = userId, noteId = id)
    }
}