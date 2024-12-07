package com.bibek.chatapplication.utils.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Interface to observe the connectivity state of the device.
 *
 * This interface provides the necessary functionality to observe the current network connectivity
 * state and listen for changes in the connectivity status. Implementations of this interface will
 * allow the app to react to connectivity changes in real-time.
 */
interface ConnectivityObserver {

    /**
     * A [Flow] that emits the current connectivity state of the device.
     *
     * This flow emits updates on the device's connectivity status, allowing other parts of the
     * application to react to changes in connectivity. It will emit either [ConnectionState.Available]
     * or [ConnectionState.Unavailable] based on whether the device has internet access or not.
     *
     * This flow should be collected to observe connectivity changes.
     */
    val connectionState: Flow<ConnectionState>

    /**
     * The current connectivity state of the device.
     *
     * This property returns the device's current network state at the time of access. It will return
     * [ConnectionState.Available] if the device has internet access, or [ConnectionState.Unavailable]
     * if the device does not have internet access.
     *
     * This property is useful for immediate access to the current connectivity state without the need
     * to observe the flow.
     */
    val currentConnectionState: ConnectionState
}
