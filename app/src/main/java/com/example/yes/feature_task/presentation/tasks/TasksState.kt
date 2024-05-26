package com.example.yes.feature_task.presentation.tasks

import com.example.yes.feature_task.domain.model.Task
import com.example.yes.util.OrderType

data class TasksState (
    val tasks:List<Task> = emptyList(),
    val isOrderSectionVisible:Boolean = false,
    val orderType: OrderType = OrderType.Descending,
)