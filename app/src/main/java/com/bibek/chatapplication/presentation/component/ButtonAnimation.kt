package com.bibek.chatapplication.presentation.component


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

enum class ButtonState { Pressed, Idle }

fun Modifier.pressClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { })
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    var buttonStateChange by remember { mutableStateOf(ButtonStateChange.Idle) }

    val scale by animateFloatAsState(if (buttonStateChange == ButtonStateChange.Pressed) 0.85f else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                onClick.invoke()
            })
        .pointerInput(buttonStateChange) {
            awaitPointerEventScope {
                buttonStateChange = if (buttonStateChange == ButtonStateChange.Pressed) {
                    waitForUpOrCancellation()
                    ButtonStateChange.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonStateChange.Pressed
                }
            }
        }
}
