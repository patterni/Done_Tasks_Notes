package com.example.yes.feature_note.presentation.notes

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.util.NoteOrder
import com.example.yes.util.OrderType

data class NotesState(
    val notes:List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible:Boolean = false
)
