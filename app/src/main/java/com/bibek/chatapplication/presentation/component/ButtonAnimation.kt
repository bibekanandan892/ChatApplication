package com.bibek.chatapplication.presentation.component


import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

enum class ButtonStateChange { Pressed, Idle }
/**
 * A custom [Modifier] extension function that adds a "bounce" effect to a clickable composable.
 * When the composable is pressed, it briefly shrinks (scales down) and then returns to its original size when released.
 * This is achieved by using an animation to scale the composable and detecting pointer events for user interaction.
 *
 * @param onClick A lambda function that is executed when the composable is clicked. It is invoked when the user releases the click.
 *
 * @return A modified [Modifier] that applies the bounce click effect to the composable.
 */
@SuppressLint("ReturnFromAwaitPointerEventScope")
fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    // State variable to track the button's state (pressed or idle)
    var buttonStateChange by remember { mutableStateOf(ButtonStateChange.Idle) }

    // Animate the scaling of the composable when pressed or idle
    val scale by animateFloatAsState(
        if (buttonStateChange == ButtonStateChange.Pressed) 0.85f else 1f,
        label = ""
    )

    // Applying the bounce effect using graphicsLayer and pointer input handling
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null, // No indication effect is applied (e.g., ripple effect)
            onClick = { onClick.invoke() } // Invoke the onClick lambda when clicked
        )
        .pointerInput(buttonStateChange) {
            awaitPointerEventScope {
                // Handle pointer events to change button state (pressed/idle)
                buttonStateChange = if (buttonStateChange == ButtonStateChange.Pressed) {
                    waitForUpOrCancellation() // Wait until the pointer is released or cancelled
                    ButtonStateChange.Idle // Set state back to idle after release
                } else {
                    awaitFirstDown(false) // Wait for the first touch down event
                    ButtonStateChange.Pressed // Set state to pressed when touch down occurs
                }
            }
        }
}
