package com.example.yes.feature_task.domain.use_case

import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository

class UpdateTask(private val repository: FirebaseTaskRepository) {
    suspend operator fun invoke(completed:Boolean, id:String?, userId:String?){
        repository.updateTask(completed= completed, taskId = id, userId = userId)
    }
}