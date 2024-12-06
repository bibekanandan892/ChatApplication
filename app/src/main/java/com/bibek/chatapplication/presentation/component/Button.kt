package com.bibek.chatapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.presentation.theme.ButtonColor
import com.bibek.chatapplication.presentation.theme.LightGray

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit = {}
) {
    Row {
        // Show button if not in loading state
        AnimatedVisibility(visible = !isLoading) {
            Box(
                modifier = modifier
                    .bounceClick(onClick)
                    .height(65.dp)
                    .background(color = ButtonColor, shape = RoundedCornerShape(4.dp))
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = text, color = Color.White)
            }
        }
        // Show CircularProgressIndicator if in loading state
        if (isLoading) {
            CircularProgressIndicator(color = LightGray)
        }
    }
}