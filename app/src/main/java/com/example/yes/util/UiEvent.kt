package com.example.yes.util

sealed class UiEvent{
    data class ShowSnackBar(val message:String): UiEvent()
    object SaveTask: UiEvent()
}