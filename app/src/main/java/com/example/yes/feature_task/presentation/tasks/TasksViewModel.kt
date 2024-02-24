package com.example.yes.feature_task.presentation.tasks

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.use_case.TasksUseCases
import com.example.yes.util.OrderType
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksUseCases: TasksUseCases
) : ViewModel() {

    private val _state = mutableStateOf(TasksState())
    val state: State<TasksState> = _state

    private var recentlyDeletedTask: Task? = null

    private val auth= Firebase.auth

    private val uid = getSignInUserId()

    private var getTasksJob: Job? = null

    init {
        getTasks(OrderType.Descending)
    }

    fun onEvent(event: TasksEvent){
        when(event){
            is TasksEvent.DeleteTask ->{
                viewModelScope.launch {
                    tasksUseCases.deleteTask(task = event.task,userId=uid)
                    recentlyDeletedTask=event.task
                }
            }
            is TasksEvent.RestoreTask ->{
                viewModelScope.launch {
                    tasksUseCases.addTask(task = recentlyDeletedTask ?:return@launch, userId=uid)
                    recentlyDeletedTask=null
                }
            }
            is TasksEvent.CompleteTask ->{
                viewModelScope.launch {
                    tasksUseCases.updateTask(completed = !event.task.completed, id = event.task.id, userId = uid )
                }
            }
            is TasksEvent.ToggleOrderSection ->{
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )


            }
            is TasksEvent.Order ->{
                if (state.value.orderType == event.orderType){
                    return
                }
                getTasks(event.orderType)
            }
        }
    }

    private fun getTasks(orderType: OrderType){
        getTasksJob?.cancel()
        getTasksJob = tasksUseCases.getTasks(orderType, userId = uid).onEach { tasks->
            _state.value=state.value.copy(
                tasks=tasks,
                orderType= orderType
            )
        }
            .launchIn(viewModelScope)
    }

    private fun getSignInUserId():String? = auth.currentUser?.run {
        uid
    }
}
