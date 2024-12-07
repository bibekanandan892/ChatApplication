package com.bibek.chatapplication.presentation.screen.search
/**
 * A sealed interface representing the different states of a WebSocket connection.
 *
 * This interface is used to track the connection state of a WebSocket, allowing the system
 * to react to changes in the connection status (whether the WebSocket is connected or disconnected).
 */
sealed interface WebSocketState {

    /**
     * Represents the state when the WebSocket is connected and active.
     */
    data object Connected : WebSocketState

    /**
     * Represents the state when the WebSocket is disconnected.
     */
    data object Disconnected : WebSocketState
}
