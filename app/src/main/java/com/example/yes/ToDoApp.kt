package com.example.yes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.yes.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.yes.feature_note.presentation.notes.NotesScreen
import com.example.yes.feature_profile.presentation.default_sign_in.SignInScreen
import com.example.yes.feature_profile.presentation.default_sign_up.SignUpScreen
import com.example.yes.feature_profile.presentation.google_sign_in.GoogleAuthUiClient
import com.example.yes.feature_profile.presentation.profile.ProfileScreen
import com.example.yes.feature_task.presentation.add_edit_task.AddEditTask
import com.example.yes.feature_task.presentation.tasks.Tasks
import com.example.yes.feature_task.presentation.util.Screen


@Composable
fun ToDoApp(navController: NavHostController, googleAuthUiClient: GoogleAuthUiClient){
    NavHost(navController = navController,
        startDestination = if(googleAuthUiClient.getSignInUser() != null) Screen.BottomBar.route else Screen.AuthScreen.route){

        navigation(route = Screen.AuthScreen.route,
            startDestination = Screen.SignUpScreen.route){

            composable(Screen.SignUpScreen.route){
                SignUpScreen(navController = navController, googleAuthUiClient = googleAuthUiClient )
            }
            composable(Screen.SignInScreen.route){
                SignInScreen(navController=navController)
            }
        }

        navigation(route=Screen.BottomBar.route,
            startDestination =Screen.TasksScreen.route){
            composable(route= Screen.TasksScreen.route){
                Tasks(navController = navController)
            }
            composable(route = Screen.NotesScreen.route){
                NotesScreen(navController = navController)
            }
            composable(route = Screen.ProfileScreen.route){
                ProfileScreen(navController = navController,googleAuthUiClient.getSignInUser())
            }
        }
        composable(route = Screen.AddEditTaskScreen.route +
                "?taskId={taskId}", arguments = listOf(
            navArgument(
                name = "taskId"
            ){
                type = NavType.StringType
                defaultValue = ""
            }
        )
        ){
            AddEditTask(navController = navController)
        }
        composable(
            route = Screen.AddEditNoteScreen.route +
                    "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name = "noteColor"
                ){
                    type = NavType.IntType
                    defaultValue = -1
                }

            )
        ){
            val color = it.arguments?.getInt("noteColor")?:-1
            AddEditNoteScreen(navController = navController,
                noteColor = color)
        }
    }
}