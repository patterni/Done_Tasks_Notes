package com.example.yes.feature_task.presentation.util

sealed class Screen(val route:String){
    object NotesScreen:Screen("notes_screen")
    object ProfileScreen:Screen("profile_screen")
    object BottomBar:Screen("bottom_bar")
    object TasksScreen: Screen("tasks_screen")
    object AddEditTaskScreen: Screen("add_edit_task_screen")
    object AddEditNoteScreen:Screen("add_edit_note_screen")
    object SignInScreen : Screen("sign_in_Screen")
    object SignUpScreen : Screen("sign_up_Screen")
    object AuthScreen : Screen("auth_Screen")





}
