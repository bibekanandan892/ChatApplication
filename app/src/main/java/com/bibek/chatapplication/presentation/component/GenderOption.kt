package com.bibek.chatapplication.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.screen.signup.Gender
import com.bibek.chatapplication.presentation.theme.Primary

/**
 * A composable that represents a gender selection option with an icon and text.
 * This component is used to display gender options, such as Male, Female, etc., with a visual indication of selection.
 *
 * @param iconRes The resource ID of the drawable icon representing the gender option.
 * @param text The label or name of the gender option (e.g., "Male", "Female").
 * @param gender The currently selected gender. This is used to highlight the selected option.
 * @param onClick A callback to handle the click event. This is invoked when the gender option is clicked.
 *
 * The composable displays:
 * - An icon representing the gender option.
 * - A label with the gender name (text).
 *
 * The option is highlighted (with a border) if it matches the currently selected gender.
 * The border color changes to [Primary] if selected, or remains [White] if not selected.
 *
 * @see Gender for the enum class representing gender types.
 */
@Composable
fun GenderOption(
    @DrawableRes iconRes: Int,
    text: String,
    gender: Gender?,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = if (text == gender?.name) Primary else White, // Change border color based on selection
                shape = CircleShape
            )
            .padding(7.dp)
            .bounceClick(onClick = onClick), // Bounce effect on click
        horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
    ) {
        // Display the icon for the gender option
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text, // Label the image with the gender name
            modifier = Modifier.size(77.dp) // Set the size of the image
        )
        // Display the text label for the gender option
        Text(text = text, color = Color.Gray)
    }
}
