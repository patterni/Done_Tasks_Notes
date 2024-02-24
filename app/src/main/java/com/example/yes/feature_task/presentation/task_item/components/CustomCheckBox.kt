package com.example.yes.feature_task.presentation.task_item.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yes.ui.theme.Blue
import com.example.yes.ui.theme.Grey


@Composable
fun CustomCheckBox(taskName:String,
                   taskCompleted:Boolean,
                   onCheckedChange:(Boolean)->Unit,
                   modifier: Modifier= Modifier){
    Row(modifier=modifier.padding(10.dp,0 .dp,0 .dp,0 .dp)) {
        RoundCheckBox(isChecked = taskCompleted,
            onClick = {
                onCheckedChange(it)
            }, modifier = Modifier.align(Alignment.CenterVertically),
            enabled = true,
            color = RoundCheckBoxDefaults.colors(selectedColor = Blue))
        if(taskCompleted){
            Text(
                text = taskName,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(textDecoration = TextDecoration.LineThrough).
                    plus(MaterialTheme.typography.bodyLarge)
                    .plus(TextStyle(fontSize = 20.sp, color = Grey)),
            )
        }else {
            Text(
                text = taskName,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp),
                maxLines = 3, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.plus(TextStyle( color = MaterialTheme.colorScheme.onPrimary)),
            )
        }
    }

}

