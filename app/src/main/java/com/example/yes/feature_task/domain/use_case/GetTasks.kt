package com.example.yes.feature_task.domain.use_case

import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository
import com.example.yes.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasks(
    private val repository: FirebaseTaskRepository
) {
    operator fun invoke(orderType: OrderType = OrderType.Descending, userId:String?): Flow<List<Task>> {
        return repository.getTasks(userId).map { tasks->
            when(orderType){
                is OrderType.Ascending ->{
                    tasks.sortedBy {it.created}
                }
                is OrderType.Descending->{
                    tasks.sortedByDescending { it.created }
                }
            }
        }
    }

}