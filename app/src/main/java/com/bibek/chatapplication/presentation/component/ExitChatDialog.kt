package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
/**
 * A composable that displays a dialog to confirm if the user wants to leave the chat.
 * This dialog is shown when [showDialog] is true and provides options to either cancel or confirm leaving the chat.
 *
 * @param showDialog A boolean flag that controls the visibility of the dialog.
 * @param onDismiss A callback to be invoked when the dialog is dismissed, either by clicking the "Cancel" button or outside the dialog.
 * @param onConfirm A callback to be invoked when the user confirms the action by clicking the "Yes" button.
 * @param message A custom message to display in the dialog. Defaults to "Are you sure you want to leave?".
 *
 * The dialog consists of a message and two buttons:
 * - "Cancel" to dismiss the dialog without taking any action.
 * - "Yes" to confirm the action (e.g., leaving the chat) and dismiss the dialog.
 *
 * The dialog is enclosed within a [Dialog] and styled with a rounded surface.
 */
@Composable
fun ExitChatDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    message: String = "Are you sure you want to leave?"
) {
    // Only show the dialog if showDialog is true
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss // Dismiss the dialog when tapping outside of it
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp) // Apply rounded corners to the dialog
            ) {
                Column(
                    modifier = Modifier.padding(16.dp), // Padding for the dialog content
                    horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
                ) {
                    Text(text = "Alert", style = TextStyle.Default) // Title text
                    Spacer(modifier = Modifier.height(16.dp)) // Spacer between title and message
                    Text(text = message) // The custom or default message
                    Spacer(modifier = Modifier.height(16.dp)) // Spacer between message and buttons
                    Row(
                        horizontalArrangement = Arrangement.Center, // Center buttons horizontally
                        modifier = Modifier.fillMaxWidth() // Make the row take full width
                    ) {
                        // Cancel button that dismisses the dialog
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp)) // Spacer between buttons
                        // Confirm button that calls onConfirm and then dismisses the dialog
                        TextButton(onClick = {
                            onConfirm() // Call the confirmation callback
                            onDismiss() // Dismiss the dialog
                        }) {
                            Text(text = "Yes")
                        }
                    }
                }
            }
        }
    }
}
