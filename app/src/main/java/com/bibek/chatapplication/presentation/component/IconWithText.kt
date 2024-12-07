package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
/**
 * A composable function that displays an icon followed by a label (text) underneath it.
 * It is designed for use cases where you need to represent an icon with an associated text label.
 *
 * @param icon A `Painter` representing the icon to be displayed.
 * @param label The text label that appears under the icon.
 * @param onClick A lambda function executed when the item is clicked. The default is an empty lambda.
 *
 * The composable renders:
 * - An `Icon` displayed with the provided `icon` painter.
 * - A `Text` displayed under the icon using the provided `label`.
 * - Both elements are center-aligned within a column layout.
 * - The `bounceClick` modifier adds a clickable animation effect to the column, triggering `onClick` when clicked.
 *
 * The icon has a fixed size of 24.dp, with a custom tint color applied.
 * The text is styled with the `caption` typography and colored gray.
 */
@Composable
fun IconWithText(icon: Painter, label: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp) // Add padding around the icon and text
            .bounceClick(onClick) // Add bounce click effect
    ) {
        Icon(
            painter = icon,
            contentDescription = label, // Content description for accessibility
            tint = Color(0xFF64B5F6), // Set the tint color for the icon
            modifier = Modifier.size(24.dp) // Set the size of the icon
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption, // Use caption typography style for the text
            color = Color.Gray // Set text color to gray
        )
    }
}
