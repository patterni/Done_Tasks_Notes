package com.example.yes.feature_task.domain.repository

import com.example.yes.feature_task.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface FirebaseTaskRepository {

    fun getTasks(userId: String?): Flow<List<Task>>

    suspend fun getTaskById(userId: String?, taskId: String): Task?

    suspend fun insertTask(userId: String?, task: Task)

    suspend fun updateTask(userId: String?, completed: Boolean, taskId: String?)

    suspend fun deleteTask(userId: String?, task: Task)
}