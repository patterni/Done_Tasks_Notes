package com.example.yes.feature_note.domain.use_case

import com.example.yes.feature_note.domain.model.InvalidNoteException
import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository


class AddNote (
    private val repository: FirebaseNoteRepository
){

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(userId:String?, note: Note){
        if(note.title.isBlank()){
            throw InvalidNoteException("The title of note can't by empty")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("The content of note can't by empty")
        }
        repository.insertNote(userId,note)
    }
}