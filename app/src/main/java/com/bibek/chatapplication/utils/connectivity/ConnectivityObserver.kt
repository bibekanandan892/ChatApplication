package com.bibek.chatapplication.utils.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val connectionState: Flow<ConnectionState>
    val currentConnectionState: ConnectionState
}