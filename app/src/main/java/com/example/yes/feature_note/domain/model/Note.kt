package com.example.yes.feature_note.domain.model

import com.example.yes.ui.theme.NoteBeige
import com.example.yes.ui.theme.NoteBlue
import com.example.yes.ui.theme.NoteLightBlue
import com.example.yes.ui.theme.NoteTeal
import com.example.yes.ui.theme.NoteYellow

data class Note(
    val title:String,
    val content:String,
    val timestamp:Long,
    val color:Int,
    val id: String? = ""
){
    companion object{
        val noteColors = listOf(NoteTeal, NoteBlue, NoteLightBlue, NoteBeige, NoteYellow)
    }
    constructor() : this(
        title = "",
        content = "",
        timestamp = System.currentTimeMillis(),
        color = -1,
    )
}


class InvalidNoteException(message:String):Exception(message)

