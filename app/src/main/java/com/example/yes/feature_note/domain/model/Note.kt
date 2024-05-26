package com.example.yes.feature_note.domain.model

import com.example.yes.ui.theme.DarkBlue
import com.example.yes.ui.theme.TestBlue
import com.example.yes.ui.theme.TestFifth
import com.example.yes.ui.theme.TestForth
import com.example.yes.ui.theme.TestGrey

data class Note(
    val title:String,
    val content:String,
    val timestamp:Long,
    val color:Int,
    val id: String? = ""
){
    companion object{
        val noteColors = listOf(TestGrey, TestBlue, TestFifth, TestForth,  DarkBlue)
    }
    constructor() : this(
        title = "",
        content = "",
        timestamp = System.currentTimeMillis(),
        color = -1,
    )
}


class InvalidNoteException(message:String):Exception(message)

