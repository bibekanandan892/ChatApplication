package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.theme.Primary

@Composable
fun GenderSelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A)) // Dark background color
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Second Button (Not Selected)
        GenderButton(
            text = "الكل",
            selected = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun GenderButton(text: String, selected: Boolean, modifier: Modifier = Modifier) {
    val backgroundColorMale =
        if (selected) Color(0xFF4A90E2) else Color(0xFF2A2A2A) // Selected or unselected background
    val backgroundColorFemale =
        if (!selected) Color(0xFF4A90E2) else Color(0xFF2A2A2A) // Selected or unselected background

    val textColor = if (selected) Color.White else Color.Gray

    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .background(backgroundColorMale),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "أنثى",
            color = textColor,
            style = MaterialTheme.typography.body1
        )
        Divider(modifier = Modifier
            .height(50.dp)
            .width(2.dp), color = Primary)
        Text(
            text = "الكل",
            color = textColor,
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSelectorPreview() {
    GenderSelector()
}
