package com.bibek.chatapplication.presentation.screen.search

sealed interface WebSocketState {
    data object Connected : WebSocketState
    data object Disconnected : WebSocketState
}