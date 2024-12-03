package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.theme.ColorTextFieldContainerDefault
import com.bibek.chatapplication.presentation.theme.ColorTextFieldText

@Preview
@Composable
fun TextBox(
    text: String = "",
    hint: String = "",
    onTextChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.widthIn(max = 500.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = hint,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End // Align text to the end (right side)
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedLabelColor = ColorTextFieldText,
                focusedIndicatorColor = ColorTextFieldContainerDefault,
                unfocusedIndicatorColor = ColorTextFieldContainerDefault,
                disabledIndicatorColor = ColorTextFieldContainerDefault,
                backgroundColor = ColorTextFieldContainerDefault
            ),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End // Align user input to the end (right side)
            )
        )
    }
}
