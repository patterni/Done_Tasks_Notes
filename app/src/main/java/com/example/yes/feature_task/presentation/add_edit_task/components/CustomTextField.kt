import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomTextField(
    text: String,
    hint: String,
    textStyle: TextStyle=TextStyle(fontSize = 16.sp),
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp)
            .background(Color.Transparent)
    ) {
        if (text.isEmpty()) {
            Text(
                text = hint,
                color = Color.Gray,
                style = textStyle
            )
        }
        BasicTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
                isExpanded = it.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isExpanded = it.isFocused
                },
            textStyle = textStyle,
            singleLine = !isExpanded,
            maxLines = if (isExpanded) 4 else 1
        )
    }
}

