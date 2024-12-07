package com.bibek.chatapplication.utils.connectivity

import android.net.ConnectivityManager
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the [ConnectivityObserver] interface that observes and provides the current
 * connectivity state of the device using [ConnectivityManager].
 *
 * This class wraps around the [ConnectivityManager] to provide the current network connectivity state
 * and listen for changes in connectivity using [observeConnectivityAsFlow].
 *
 * @param connectivityManager The [ConnectivityManager] instance used to observe network connectivity.
 */
class ConnectivityObserverImpl(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    /**
     * A [Flow] representing the connectivity state of the device.
     *
     * This flow emits updates on the device's connectivity status, using the [ConnectivityManager]'s
     * [observeConnectivityAsFlow] function to listen for network state changes.
     *
     * The flow emits [ConnectionState.Available] or [ConnectionState.Unavailable] depending on whether
     * the device has internet access.
     */
    override val connectionState: Flow<ConnectionState>
        get() = connectivityManager.observeConnectivityAsFlow()

    /**
     * The current connectivity state of the device.
     *
     * This property checks the device's current network state, returning either [ConnectionState.Available]
     * if the device has internet access, or [ConnectionState.Unavailable] if it does not.
     */
    override val currentConnectionState: ConnectionState
        get() = connectivityManager.currentConnectivityState
}
