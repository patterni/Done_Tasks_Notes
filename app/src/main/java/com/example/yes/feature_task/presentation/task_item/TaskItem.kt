package com.example.yes.feature_task.presentation.task_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yes.R
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.presentation.task_item.components.CustomCheckBox
import com.example.yes.ui.theme.YesTheme

@Composable
fun TaskItem(task: Task,
             modifier: Modifier,
             taskCompleted:Boolean,
             onDeleteClick: () ->Unit,
             onCheckedChange:(Boolean)->Unit){

    Surface(shape = RoundedCornerShape(30),
        color = if (taskCompleted) {MaterialTheme.colorScheme.secondary }
                else MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(70.dp)) {
        Row(modifier.fillMaxWidth()) {
            CustomCheckBox(taskName = task.name,
                taskCompleted = taskCompleted,
                onCheckedChange =onCheckedChange,
                modifier.weight(2f,true))
            if(task.important) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_fiber_manual_record_24),
                    contentDescription = stringResource(id = R.string.task_importance_mark),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
            IconButton(onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 4.dp)) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_task,),
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

}
