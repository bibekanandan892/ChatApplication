package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.presentation.theme.LightGray


/**
 * A composable function that displays a received message with the message text and timestamp.
 * The message is presented in a bubble-like style with a background and border, commonly used in chat interfaces.
 *
 * @param message The text content of the received message.
 * @param time The timestamp when the message was received.
 *
 * The composable renders:
 * - A `Box` containing the message text and the timestamp.
 * - The message text is displayed in a bubble with rounded corners, with padding and border.
 * - The timestamp is shown below the message text in a smaller, gray font.
 *
 * The message bubble is styled with a background color and border, while the text and timestamp are styled accordingly.
 */
@Composable
fun ReceivedMessage(message: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Make the row take full width
            .padding(8.dp), // Add padding around the row
        horizontalArrangement = Arrangement.Start // Align items to the start of the row
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFECEFF1), shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        ) // Set background with rounded corners
                    )
                    .border(
                        width = 1.dp, shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        ), color = LightGray.copy(0.1f) // Add a subtle border with rounded corners
                    )
                    .padding(8.dp) // Add padding inside the message box
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = message, color = Color.Black) // Display message in black color
                    Spacer(modifier = Modifier.height(4.dp)) // Add space between message and timestamp
                    Row(
                        modifier = Modifier.padding(start = 8.dp), // Add left padding for the timestamp
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Add space between the timestamp and any elements in the row
                    ) {
                        Text(text = time, color = Color.Gray, fontSize = 12.sp) // Display the timestamp in gray and smaller font
                    }
                }
            }
        }
    }
}
