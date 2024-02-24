package com.example.yes.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yes.feature_note.domain.model.InvalidNoteException
import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.use_case.NotesUseCases
import com.example.yes.util.TextFieldState
import com.example.yes.util.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val _noteTitle = mutableStateOf(TextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<TextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(TextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<TextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors[0].toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val auth= Firebase.auth
    private val uid = auth.currentUser?.run { uid }

    private var currentNoteId: String? = null

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId->
            if(noteId!=""){
                viewModelScope.launch {
                    notesUseCases.getNote(id = noteId, userId = uid)?.also {note->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event:AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle->{
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus->{
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                        noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent->{
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus->{
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor->{
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote->{
                viewModelScope.launch {
                    try {
                        notesUseCases.addNote(
                            note = Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            ), userId = uid
                        )
                        _eventFlow.emit(UiEvent.SaveTask)
                    }catch (e:InvalidNoteException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(message = e.message?:"Couldn't save note")
                        )
                    }
                }
            }
        }
    }


}