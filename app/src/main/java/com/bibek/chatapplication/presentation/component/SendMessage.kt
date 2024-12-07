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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.presentation.screen.search.Message
import com.bibek.chatapplication.presentation.theme.LightGray

/**
 * A composable function that displays a sent message with a timestamp and message status.
 * The message is displayed within a stylized bubble with an optional status icon
 * (e.g., "Sending", "Sent", or "Read").
 *
 * @param message The message content to be displayed.
 * @param time The timestamp indicating when the message was sent.
 * @param status The status of the message, represented by an instance of the [Message] enum.
 *               The status can be `Sending`, `Sent`, `Read`, or `None`.
 *
 * The composable renders:
 * - A `Text` widget displaying the sent message content.
 * - A timestamp (`Text` widget) showing when the message was sent.
 * - An optional status indicator (e.g., "⏳" for sending, "✓" for sent, or "✓✓" for read).
 * - A stylized message bubble with rounded corners and a light blue background, depending on the status.
 *
 * The layout aligns the message bubble to the right side of the screen (using [Arrangement.End]).
 *
 * @see Message
 */
@Composable
fun SentMessage(message: String, time: String, status: Message?) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Make the row take up the full width
            .padding(8.dp), // Add padding around the row
        horizontalArrangement = Arrangement.End // Align the message bubble to the right
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFBBDEFB), // Set the background color to light blue
                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        ) // Apply rounded corners to the message bubble
                    )
                    .border(
                        width = 1.dp, // Add a 1dp border around the bubble
                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        ),
                        color = LightGray.copy(0.1f) // Light gray border color
                    )
                    .padding(8.dp) // Add padding inside the message bubble
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = message, color = Color.Black) // Display the message content
                    Spacer(modifier = Modifier.height(4.dp)) // Add spacing between message and time/status
                    Row(
                        horizontalArrangement = Arrangement.End, // Align content to the right
                        verticalAlignment = Alignment.CenterVertically // Vertically center the content
                    ) {
                        Text(
                            text = time, // Display the timestamp
                            color = Color.Gray, // Gray color for the timestamp
                            fontSize = 12.sp // Set the font size to 12sp
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Add spacing between time and status
                        Text(
                            text = when (status) { // Display message status icon
                                Message.Sending -> "⏳" // "Sending" status
                                Message.Sent -> "✓" // "Sent" status
                                Message.Read -> "✓✓" // "Read" status
                                Message.None -> "" // No status
                                else -> "" // Default case for no status
                            },
                            color = Color.Gray, // Gray color for the status icon
                            fontSize = 12.sp // Set the font size to 12sp
                        )
                    }
                }
            }
        }
    }
}
