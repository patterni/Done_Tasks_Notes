package com.example.yes.feature_task.domain.model



import java.text.DateFormat


data class Task(
    val name: String,
    val important:Boolean = false,
    val completed: Boolean = false,
    val date:Long? = null,
    val time: TaskTime? = null,
    val created: Long = System.currentTimeMillis(),
    val id: String? = ""
){
    val createdDateFormatted:String
        get() = DateFormat.getDateTimeInstance().format(created)


    constructor() : this(
        name = "",
        important = false,
        completed = false,
        date = null,
        time = null,
        created = System.currentTimeMillis(),
        id = ""
    )
}
data class TaskTime(
    val hour: Int,
    val minute: Int
){
    // Add a no-argument constructor
    constructor() : this(-1, -1)
}

class InvalidTaskException(message:String):Exception(message)