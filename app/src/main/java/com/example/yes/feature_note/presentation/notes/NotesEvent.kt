package com.example.yes.feature_note.presentation.notes

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.util.NoteOrder

sealed class NotesEvent{
    data class Order(val noteOrder: NoteOrder):NotesEvent()
    data class DeleteNote(val note: Note):NotesEvent()
    object RestoreNote:NotesEvent()
    object ToggleOrderSection:NotesEvent()
}
