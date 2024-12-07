package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that creates a customizable action button.
 *
 * This button is designed to be used for various actions in the UI, with
 * customization options for the button text, background color, and the
 * click action. It is styled with a circular shape and a fixed size.
 *
 * @param text The text to be displayed inside the button.
 * @param backgroundColor The background color of the button.
 * @param onClick A lambda function to be executed when the button is clicked.
 * By default, this is an empty lambda that does nothing.
 */
@Composable
fun ActionButton(text: String, backgroundColor: Color, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        shape = CircleShape
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}
