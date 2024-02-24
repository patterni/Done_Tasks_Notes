package com.example.yes.feature_task.presentation.tasks

import com.example.yes.feature_task.domain.model.Task
import com.example.yes.util.OrderType

sealed class TasksEvent{
    data class DeleteTask(val task: Task): TasksEvent()
    data object RestoreTask: TasksEvent()
    data class CompleteTask(val task: Task): TasksEvent()
    data object ToggleOrderSection: TasksEvent()
    data class Order(val orderType: OrderType): TasksEvent()
}
