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

@Composable
fun RightAlignText(text : String = "",horizontalAlignment: Alignment.Horizontal = Alignment.End) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text =text,
            style = MaterialTheme.typography.subtitle1,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
