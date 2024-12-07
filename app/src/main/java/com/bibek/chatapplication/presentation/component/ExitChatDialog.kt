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

@Composable
fun ExitChatDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    message : String = "Are you sure you want to leave?"
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Text(text = "Alert", style = TextStyle.Default)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = message)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) {
                           Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(onClick = {
                            onConfirm()
                            onDismiss()
                        }) {
                            Text(text = "Yes")
                        }
                    }
                }
            }
        }
    }
}
