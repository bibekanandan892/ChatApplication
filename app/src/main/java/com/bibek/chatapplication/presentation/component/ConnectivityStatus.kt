package com.bibek.chatapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.utils.connectivity.ConnectionState
import com.bibek.chatapplication.R
import kotlinx.coroutines.delay

/**
 * A composable that displays a connectivity status notification based on the current [connectionState].
 * The notification is shown when there is no internet connection and automatically disappears after 2 seconds
 * when the connection is restored.
 *
 * @param connectionState The current connection state, which can either be [ConnectionState.Available] or [ConnectionState.Unavailable].
 *
 * This composable uses [AnimatedVisibility] to animate the appearance and disappearance of the connectivity status box,
 * and it updates its visibility based on the connection state.
 */
@Composable
fun ConnectivityStatus(connectionState: ConnectionState) {
    // State to manage the visibility of the connectivity status box
    var visibility by remember { mutableStateOf(false) }

    // AnimatedVisibility to show or hide the status box with animation
    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(connectionState = connectionState)
    }

    // LaunchedEffect to update visibility based on connection state
    LaunchedEffect(connectionState) {
        visibility = when (connectionState) {
            ConnectionState.Available -> {
                delay(2000)  // Wait for 2 seconds before hiding the status
                false
            }
            ConnectionState.Unavailable -> true  // Show status when no internet
        }
    }
}
/**
 * A composable that displays a box indicating the connectivity status with a background color,
 * an icon, and a message based on the [connectionState].
 *
 * @param connectionState The current connection state, which can either be [ConnectionState.Available] or [ConnectionState.Unavailable].
 *
 * This composable shows a message like "Back Online!" when connected and "No Internet Connection!" when disconnected,
 * along with corresponding icons and colors (green for connected, red for disconnected).
 */
@Composable
fun ConnectivityStatusBox(connectionState: ConnectionState) {
    // Determine the color based on the connection state
    val isConnected = if (connectionState is ConnectionState.Available) Color.Green else Color.Red
    val backgroundColor by animateColorAsState(isConnected, label = "")

    // Message to display based on connection state
    val message = when (connectionState) {
        ConnectionState.Available -> "Back Online!"
        ConnectionState.Unavailable -> "No Internet Connection!"
    }

    // Icon to display based on connection state
    val iconResource = when (connectionState) {
        ConnectionState.Available -> R.drawable.internet  // Replace with actual drawable resource for connected state
        ConnectionState.Unavailable -> R.drawable.no_internet  // Replace with actual drawable resource for disconnected state
    }

    // Box displaying the connection status
    Box(
        modifier = Modifier
            .background(backgroundColor)  // Apply background color based on connection state
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Row to display the icon and message
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon based on connection state
            Icon(
                painterResource(id = iconResource),
                "Connectivity Icon", tint = Color.White
            )
            Spacer(modifier = Modifier.size(8.dp))  // Add space between icon and message
            // Message based on connection state
            Text(message, color = Color.White, fontSize = 15.sp)
        }
    }
}
