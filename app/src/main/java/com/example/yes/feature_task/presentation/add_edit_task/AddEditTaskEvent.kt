package com.example.yes.feature_task.presentation.add_edit_task

import com.example.yes.feature_task.domain.model.TaskTime
import java.time.LocalDate


sealed class AddEditTaskEvent{
    data class EnteredTitle(val value:String): AddEditTaskEvent()
    data class ImportantChecked(val value:Boolean): AddEditTaskEvent()
    data class DateSelected(val date:LocalDate):AddEditTaskEvent()
    data class TimeSelected(val time: TaskTime):AddEditTaskEvent()
    object SaveTask: AddEditTaskEvent()
}
