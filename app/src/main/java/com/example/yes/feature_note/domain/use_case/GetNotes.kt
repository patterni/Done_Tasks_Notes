package com.example.yes.feature_note.domain.use_case

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository
import com.example.yes.util.NoteOrder
import com.example.yes.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes( private val repository: FirebaseNoteRepository) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        userId:String?
    ): Flow<List<Note>> {
        return repository.getNotes(userId).map { notes->
            when(noteOrder.orderType){
                is OrderType.Ascending->{
                    when(noteOrder){
                        is NoteOrder.Title-> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date-> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color-> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending->{
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}