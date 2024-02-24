package com.example.yes.feature_note.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.use_case.NotesUseCases
import com.example.yes.util.NoteOrder
import com.example.yes.util.OrderType
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
):ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private val auth= Firebase.auth

    private val uid = auth.currentUser?.run { uid }

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }


    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order->{
                if(state.value.noteOrder::class==event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote->{
                viewModelScope.launch {
                    notesUseCases.deleteNote(note = event.note, userId = uid)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote->{
                viewModelScope.launch {
                    notesUseCases.addNote(note = recentlyDeletedNote?:return@launch, userId = uid)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection->{
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
                Log.d( "DEBUG",  "${_state.value.isOrderSectionVisible}")
            }

        }

    }

    private fun getNotes(noteOrder:NoteOrder){
        getNotesJob?.cancel()
        getNotesJob =  notesUseCases.getNotes(noteOrder, userId = uid)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

}