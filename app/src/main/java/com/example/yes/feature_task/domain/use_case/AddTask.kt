package com.example.yes.feature_task.domain.use_case

import com.example.yes.feature_task.domain.model.InvalidTaskException
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository

class AddTask (
    private val repository: FirebaseTaskRepository
){

    @Throws(InvalidTaskException::class)
    suspend operator fun invoke(userId:String?, task: Task){
        if(task.name.isBlank()){
            throw InvalidTaskException("The title of task can't by empty")
        }
        repository.insertTask(userId,task)
    }
}