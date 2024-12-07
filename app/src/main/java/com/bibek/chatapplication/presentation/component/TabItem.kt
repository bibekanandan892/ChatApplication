package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.screen.signup.Gender
/**
 * A composable function that represents a tab item, which consists of a label (text) and an icon.
 * The text color and font weight change based on the `isSelected` parameter to visually distinguish the selected tab.
 *
 * The function also dynamically changes the icon based on the `text` parameter, using different icons for gender-related options.
 *
 * @param text The label text for the tab, which is used to identify the tab and to determine the corresponding icon.
 * @param isSelected A Boolean flag indicating whether the tab is currently selected or not. If `true`, the tab is selected,
 *                   and the text appears bold and white, otherwise it appears gray with normal font weight.
 *
 * The composable renders:
 * - A `Text` widget displaying the `text` passed to it, with dynamic styling based on the selection state.
 * - An icon displayed next to the text, which changes according to the `text` value (for gender-related tabs like "Male", "Female", and "Both").
 *
 * The layout arranges the text and icon horizontally, centered along the main axis and vertically aligned.
 *
 * @see Gender
 */
@Composable
fun TabItem(text: String, isSelected: Boolean) {
    Row(
        horizontalArrangement = Arrangement.Center, // Center the contents horizontally
        verticalAlignment = Alignment.CenterVertically // Align the contents vertically at the center
    ) {
        Text(
            text = text, // Display the tab label text
            color = if (isSelected) Color.White else Color.Gray, // Change the color based on selection state
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, // Bold if selected, normal if not
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp) // Add padding around the text
        )
        Image(
            painter = when (text) { // Select the appropriate icon based on the text value
                Gender.Both.name -> painterResource(R.drawable.ic_both_male_female) // Icon for 'Both'
                Gender.Male.name -> painterResource(R.drawable.ic_male) // Icon for 'Male'
                Gender.Female.name -> painterResource(R.drawable.ic_female) // Icon for 'Female'
                else -> painterResource(R.drawable.ic_both_male_female) // Default icon for 'Both'
            },
            contentDescription = null, // No content description for the icon
            Modifier.size(30.dp) // Set the icon size
        )
    }
}
