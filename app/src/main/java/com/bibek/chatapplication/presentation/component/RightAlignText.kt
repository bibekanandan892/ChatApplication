package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays text aligned horizontally based on the specified alignment.
 * The text is displayed with customizable alignment and padding to achieve a neat presentation.
 *
 * @param text The text to be displayed. Defaults to an empty string.
 * @param horizontalAlignment The horizontal alignment of the text within the column.
 *                            Defaults to [Alignment.End], which right-aligns the text.
 *
 * The composable renders:
 * - A `Text` widget that can be aligned to the left or right, depending on the specified alignment.
 * - The `Text` is displayed with a gray color and styled using the default subtitle style from the material theme.
 * - Padding is applied around the text to ensure appropriate spacing.
 */
@Composable
fun RightAlignText(text : String = "", horizontalAlignment: Alignment.Horizontal = Alignment.End) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Make the column take up the full width of the parent
            .padding(horizontal = 16.dp), // Add horizontal padding to the column
        horizontalAlignment = horizontalAlignment // Set horizontal alignment for the text
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // Add space before the text

        Text(
            text = text, // Text to be displayed
            style = MaterialTheme.typography.subtitle1, // Use subtitle1 style from Material Theme
            color = Color.Gray, // Set the text color to gray
            modifier = Modifier.padding(bottom = 8.dp) // Add bottom padding to the text
        )
    }
}
