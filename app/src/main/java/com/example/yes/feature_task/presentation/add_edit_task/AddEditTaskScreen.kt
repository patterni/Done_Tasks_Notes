package com.example.yes.feature_task.presentation.add_edit_task

import CustomTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yes.R
import com.example.yes.feature_task.domain.model.TaskTime
import com.example.yes.feature_task.presentation.task_item.components.RoundCheckBox
import com.example.yes.feature_task.presentation.task_item.components.RoundCheckBoxColors
import com.example.yes.util.UiEvent
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTask(
    navController: NavController,
    viewModel: AddEditViewModel = hiltViewModel()
){
    val titleState = viewModel.taskTitle.value
    val taskImportance = viewModel.taskImportance.value
    val scaffoldState = rememberScaffoldState()
    val dateState = viewModel.taskDate.value
    val timeState = viewModel.taskTime.value



    val dateCreated = viewModel.dateCreated


    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event->
            when(event){
                is UiEvent.ShowSnackBar ->{
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.SaveTask ->{
                    navController.navigateUp()
                }
            }
        }
    }
    val context = LocalContext.current
    Scaffold (floatingActionButton = {
        ExtendedFloatingActionButton(
            text = {Text(stringResource(id = R.string.save))},
            onClick = {
                viewModel.onEvent(AddEditTaskEvent.SaveTask)
                      },
            modifier = Modifier
                .padding(16.dp),
            icon =  { Icon(imageVector = Icons.Default.Save,
                contentDescription = stringResource(id = R.string.fab))}
        )
    }, scaffoldState = scaffoldState
    ){ padding->
        Column (modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)){

            CustomTextField(text = titleState.text,
                hint = titleState.hint ,
                onValueChange = {
                    viewModel.onEvent(AddEditTaskEvent.EnteredTitle(it))
                },
                textStyle = TextStyle(fontFamily =
                FontFamily.SansSerif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.padding(8.dp)
            )


            Row(modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)) {
                RoundCheckBox(isChecked = taskImportance, onClick ={
                    viewModel.onEvent(AddEditTaskEvent.ImportantChecked(it))
                }, color = RoundCheckBoxColors(
                    selectedColor = Color.Red,
                    disabledSelectedColor = Color(220, 219, 220),
                    disabledUnselectedColor= Color.Transparent,
                    tickColor = Color.White,
                    borderColor= Color(53, 61, 53))
                )
                Text(
                    text = stringResource(id = R.string.important_task),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            
            val calendarState = rememberSheetState()
            CalendarDialog(
                state = calendarState,
                config = CalendarConfig(
                    monthSelection = true,
                    yearSelection = true
                ),
                selection = CalendarSelection.Date {date ->
                    viewModel.onEvent(AddEditTaskEvent.DateSelected(date = date))
                }
            )
            Row {
                IconButton(onClick = {calendarState.show()}) {
                    Icon(imageVector = Icons.Default.CalendarMonth,
                        contentDescription =stringResource(id = R.string.calendar),
                        tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = viewModel.formatDate(dateState),
                    modifier= Modifier.align(Alignment.CenterVertically),
                    color=MaterialTheme.colorScheme.onPrimary)
            }

            val clockState = rememberSheetState()
            ClockDialog(state = clockState,
                config = ClockConfig(
                    is24HourFormat = true
                ),
                selection = ClockSelection.HoursMinutes{hours, minutes ->
                    viewModel.onEvent(AddEditTaskEvent.TimeSelected(TaskTime(hours,minutes)))
                } )

            Row {
                IconButton(onClick = {clockState.show()}) {
                    Icon(imageVector = Icons.Default.AccessTime,
                        contentDescription =stringResource(id = R.string.clock),
                        tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = viewModel.formatTime(timeState),
                    modifier= Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onPrimary)
            }

            if(dateCreated!="") {
                Text(
                    text = stringResource(id = R.string.date_created) + dateCreated,
                    modifier = Modifier.padding(16.dp, 0.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    }
}
