package com.example.yes.feature_note.domain.use_case

data class NotesUseCases (
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)