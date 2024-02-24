package com.example.yes.feature_task.domain.use_case

data class TasksUseCases (
    val getTasks: GetTasks,
    val deleteTask: DeleteTask,
    val addTask: AddTask,
    val getTask: GetTask,
    val updateTask: UpdateTask
)