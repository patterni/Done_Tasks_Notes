package com.example.yes.feature_task.domain.use_case

import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository

class DeleteTask(private val repository: FirebaseTaskRepository) {

    suspend operator fun invoke(userId:String?, task: Task){
        repository.deleteTask(userId,task)
    }
}