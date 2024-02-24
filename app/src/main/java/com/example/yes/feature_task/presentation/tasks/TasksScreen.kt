package com.example.yes.feature_task.presentation.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yes.R
import com.example.yes.feature_task.presentation.BottomNavigationBar
import com.example.yes.feature_task.presentation.task_item.TaskItem
import com.example.yes.feature_task.presentation.tasks.components.OrderSection
import com.example.yes.feature_task.presentation.util.Screen
import com.example.yes.ui.theme.FABColor
import com.example.yes.ui.theme.LightGrey
import kotlinx.coroutines.launch


@Composable
fun Tasks(
          navController:NavController,
          viewModel: TasksViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)},
        floatingActionButton = {
        ExtendedFloatingActionButton(
            text = {Text(stringResource(id = R.string.add))},
            onClick = {navController.navigate(Screen.AddEditTaskScreen.route)},
            modifier = Modifier
                .padding(16.dp),
            icon =  { Icon(Icons.Filled.Add, stringResource(id = R.string.fab)) },
            containerColor = FABColor,
            contentColor = LightGrey
        )
    }, bottomBar = {
            BottomNavigationBar(navController = navController)
    }) {padding->
        Column (modifier = Modifier
            .padding(padding)
            .background(MaterialTheme.colorScheme.background)){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(text = stringResource(id = R.string.tasks),
                    modifier = Modifier.padding(22.dp,12.dp,12.dp,8.dp),
                    style = MaterialTheme.typography.bodyLarge.
                    plus(TextStyle(fontSize = 35.sp, fontWeight = FontWeight.Bold))
                )
                IconButton(onClick = { viewModel.onEvent(TasksEvent.ToggleOrderSection) },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 22.dp)) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = stringResource(id = R.string.sort))
                }
            }
            AnimatedVisibility(visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()) {
               OrderSection(orderType = state.orderType,
                   onOrderChange = {
                       viewModel.onEvent(TasksEvent.Order(it))
                   }, modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 16.dp)
               )
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.tasks) { task ->
                    var completionState by rememberSaveable { mutableStateOf(task.completed) }
                    TaskItem(task = task,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditTaskScreen.route +
                                            "?taskId=${task.id}"
                                )
                            }, onDeleteClick = {
                            viewModel.onEvent(TasksEvent.DeleteTask(task))
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Task deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == ActionPerformed) {
                                    viewModel.onEvent(TasksEvent.RestoreTask)
                                }
                            }
                        }, taskCompleted =completionState ,onCheckedChange = {
                            completionState=it
                            viewModel.onEvent(TasksEvent.CompleteTask(task))
                        }
                    )
                }
            }
        }
    }
}
