package com.bibek.chatapplication.utils.connectivity

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}