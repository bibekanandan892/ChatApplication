package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.theme.ColorTextFieldContainerDefault
import com.bibek.chatapplication.presentation.theme.ColorTextFieldText

/**
 * A composable function that creates a customizable text input field with a hint.
 * The text field allows users to input text, and the hint text is displayed when the input is empty.
 * The input and hint text are right-aligned.
 *
 * @param text The current value of the text field. This value is displayed as the input text.
 * @param hint The hint text that is displayed when the text field is empty. It provides a prompt or description for the user.
 * @param onTextChange A lambda function that is called when the user changes the text in the field.
 *                     This function is used to update the value of `text` outside this composable.
 *
 * The composable renders:
 * - A `TextField` for text input, with a customizable hint text.
 * - The text field has a rounded shape, and its background color and indicator colors are customizable.
 * - The hint text and user input are both aligned to the right side of the text field.
 *
 * @see TextField
 */
@Composable
fun TextBox(
    text: String = "", // Default value for the text input
    hint: String = "", // Default value for the hint text
    onTextChange: (String) -> Unit = {} // Callback to handle text changes
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // Make the row take up the full width
        verticalAlignment = Alignment.CenterVertically, // Vertically center the text field within the row
        horizontalArrangement = Arrangement.Center // Horizontally center the row contents
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(), // Fill the width of the parent container
            value = text, // Display the current text value in the text field
            onValueChange = onTextChange, // Call onTextChange when the text is updated
            placeholder = {
                Text(
                    text = hint, // Display the hint text when the field is empty
                    modifier = Modifier.fillMaxWidth(), // Fill the width of the text field
                    textAlign = TextAlign.End // Align the hint text to the right
                )
            },
            shape = RoundedCornerShape(20.dp), // Set the shape of the text field with rounded corners
            colors = TextFieldDefaults.textFieldColors(
                unfocusedLabelColor = ColorTextFieldText, // Color for the label when the text field is unfocused
                focusedIndicatorColor = ColorTextFieldContainerDefault, // Color for the indicator when focused
                unfocusedIndicatorColor = ColorTextFieldContainerDefault, // Color for the indicator when unfocused
                disabledIndicatorColor = ColorTextFieldContainerDefault, // Color for the indicator when disabled
                backgroundColor = ColorTextFieldContainerDefault // Background color of the text field
            ),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End // Align the user input text to the right
            )
        )
    }
}
