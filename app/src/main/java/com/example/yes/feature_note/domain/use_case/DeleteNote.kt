package com.example.yes.feature_note.domain.use_case

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository

class DeleteNote (
    private val repository:FirebaseNoteRepository
){
    suspend operator fun invoke(userId:String?, note: Note){
        repository.deleteNote(userId,note)
    }
}