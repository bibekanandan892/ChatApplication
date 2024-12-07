package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.theme.Primary

/**
 * A composable function that displays a header with text inside a card layout.
 * The header is styled with a title and padding, making it ideal for sections or titles within a screen.
 *
 * @param text The text to be displayed as the header. The default value is an empty string.
 *
 * The composable renders:
 * - A `Card` container that spans the full width of the screen.
 * - A `Column` that centers the content and applies horizontal padding.
 * - A `Text` displaying the `text` parameter with a typography style (`h5`) and a primary color.
 * - Spacers are used to add vertical spacing around the header text for better layout structure.
 */
@Composable
fun Header(text: String = "") {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Add padding to the left and right
            horizontalAlignment = Alignment.End, // Align the text to the end (right) of the column
            verticalArrangement = Arrangement.Center // Center the content vertically
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Add space above the text
            Text(
                text = text,
                style = MaterialTheme.typography.h5, // Use h5 typography style for the text
                color = Primary, // Set text color to Primary color
                modifier = Modifier.padding(vertical = 8.dp) // Add vertical padding around the text
            )
            Spacer(modifier = Modifier.height(32.dp)) // Add space below the text
        }
    }
}
