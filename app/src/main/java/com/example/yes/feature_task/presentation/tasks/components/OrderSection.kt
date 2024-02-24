package com.example.yes.feature_task.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.yes.R
import com.example.yes.feature_task.presentation.util.DefaultRadioButton
import com.example.yes.util.OrderType

@Composable
fun OrderSection(
    modifier:Modifier = Modifier,
    orderType: OrderType = OrderType.Descending,
    onOrderChange:(OrderType) ->Unit
){
    Column(modifier=modifier) {
        Row (modifier = Modifier.fillMaxWidth(),Arrangement.SpaceEvenly){
            DefaultRadioButton(text = stringResource(id = R.string.oldest_first),
                selected = orderType is OrderType.Ascending,
                onSelect ={onOrderChange(OrderType.Ascending)})
            DefaultRadioButton(text = stringResource(id = R.string.newest_first),
                selected = orderType is OrderType.Descending,
                onSelect ={onOrderChange(OrderType.Descending)})
        }
    }
}