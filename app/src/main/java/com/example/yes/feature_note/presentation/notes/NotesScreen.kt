package com.example.yes.feature_note.presentation.notes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.yes.R
import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.presentation.notes.components.NoteItem
import com.example.yes.feature_note.presentation.notes.components.NotesOrderSection
import com.example.yes.feature_task.presentation.BottomNavigationBar
import com.example.yes.feature_task.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
){
    val state = viewModel.state.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)},
        snackbarHost = {snackbarHostState}, 
        floatingActionButton = {FloatingActionButton(onClick = {
            navController.navigate(Screen.AddEditNoteScreen.route)
        }){
            Icon(imageVector = Icons.Default.Add, contentDescription =  stringResource(id = R.string.add_note))}
        }) {paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text =  stringResource(id = R.string.your_notes),
                    style = MaterialTheme.typography.bodyLarge.
                    plus(
                        TextStyle(fontSize = 35.sp, fontWeight = FontWeight.Bold)),
                    modifier = Modifier.padding(12.dp,12.dp,12.dp,8.dp)
                )
                
                IconButton(onClick = {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                   }, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Sort,
                        contentDescription =  stringResource(id = R.string.sort))
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn()+ slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                NotesOrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)){
                items(state.notes){note->
                    NoteItem(note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route+
                                    "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            }, onDeleteClick = {
                                viewModel.onEvent(NotesEvent.DeleteNote(note))
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "Undo"
                                    )
                                    if(result==SnackbarResult.ActionPerformed){
                                        viewModel.onEvent(NotesEvent.RestoreNote)
                                    }
                                }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }
    }
}
