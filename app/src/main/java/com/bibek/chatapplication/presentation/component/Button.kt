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

/**
 * A composable function that represents a button with a text label.
 * The button displays a loading indicator when in a loading state.
 *
 * When the `isLoading` parameter is true, the button is hidden and a `CircularProgressIndicator`
 * is shown instead. Otherwise, the button with the provided text is displayed.
 *
 * @param modifier A [Modifier] to customize the layout of the button. By default, it is set to [Modifier].
 * @param text The text to display on the button.
 * @param isLoading A boolean indicating if the button should show a loading indicator.
 *                  When true, the button is replaced with a circular progress indicator.
 * @param onClick A lambda function that is executed when the button is clicked. By default, it does nothing.
 */
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
