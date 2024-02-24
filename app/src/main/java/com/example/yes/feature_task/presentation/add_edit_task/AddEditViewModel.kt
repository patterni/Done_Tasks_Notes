package com.example.yes.feature_task.presentation.add_edit_task

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yes.feature_task.domain.model.InvalidTaskException
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.model.TaskTime
import com.example.yes.feature_task.domain.use_case.TasksUseCases
import com.example.yes.util.TextFieldState
import com.example.yes.util.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val tasksUseCases: TasksUseCases,
    savedStateHandle: SavedStateHandle
):ViewModel(){
    private val _taskTitle = mutableStateOf(
        TextFieldState(
        hint = "The task..."
    )
    )

    val taskTitle: State<TextFieldState> = _taskTitle


    private val _taskImportance = mutableStateOf(false)
    val taskImportance:State<Boolean> = _taskImportance

     var dateCreated:String = ""

    private val _taskDate = mutableStateOf<Long?>(null)
    val taskDate:State<Long?> = _taskDate

    private val _taskTime = mutableStateOf<TaskTime?>(null)
    val taskTime:State<TaskTime?> = _taskTime

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentTaskId:String? = null

    private val auth= Firebase.auth

    private val uid = auth.currentUser?.run { uid }




    init {
        savedStateHandle.get<String>("taskId")?.let {taskId->
            if(taskId!=""){
                viewModelScope.launch {
                    tasksUseCases.getTask(id=taskId, userId = uid)?.also { task ->
                        currentTaskId = task.id
                        _taskTitle.value = taskTitle.value.copy(
                            text = task.name,
                            isHintVisible = false
                        )
                        dateCreated = task.createdDateFormatted
                        _taskImportance.value = task.important

                        _taskDate.value=task.date
                        _taskTime.value=task.time
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditTaskEvent){
        when(event){
            is AddEditTaskEvent.EnteredTitle ->{
                _taskTitle.value = taskTitle.value.copy(
                    text=event.value
                )
            }
            is AddEditTaskEvent.ImportantChecked ->{
                _taskImportance.value = !_taskImportance.value
            }
            is AddEditTaskEvent.SaveTask ->{
                viewModelScope.launch {
                    try {
                        tasksUseCases.addTask(task=
                            Task(
                                name = taskTitle.value.text,
                                important = _taskImportance.value,
                                id=currentTaskId,
                                date = taskDate.value,
                                time = taskTime.value
                            ), userId = uid
                        )
                        _eventFlow.emit(UiEvent.SaveTask)
                    }catch (e: InvalidTaskException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditTaskEvent.DateSelected->{
                val epoch: Long = event.date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
                _taskDate.value = epoch
            }
            is AddEditTaskEvent.TimeSelected->{
                val time = TaskTime(event.time.hour,event.time.minute)
                _taskTime.value = time
            }

            else -> {}
        }
    }
    fun formatTime(time: TaskTime?): String {
        if (time == null) {
            return "Time"
        }

        val formattedHours = String.format("%02d", time.hour)
        val formattedMinutes = String.format("%02d", time.minute)

        return "$formattedHours:$formattedMinutes"
    }

    fun formatDate(epoch: Long?):String{
        if (epoch == null) {
            return "Date"
        }

        val date = LocalDate.ofEpochDay(epoch/86000)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        return date.format(formatter)
    }
}

