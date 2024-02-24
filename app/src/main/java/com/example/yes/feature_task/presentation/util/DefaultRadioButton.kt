package com.example.yes.feature_task.presentation.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.yes.ui.theme.DarkBlue

@Composable
fun DefaultRadioButton(
    text:String,
    selected:Boolean,
    onSelect:()->Unit,
    modifier: Modifier = Modifier
){
    Row(modifier, verticalAlignment = Alignment.CenterVertically){
        RadioButton(selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = DarkBlue,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(text = text, style = TextStyle(fontFamily = FontFamily.SansSerif,fontWeight = FontWeight.Medium,
            fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary),)
    }
}