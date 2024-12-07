package com.bibek.chatapplication.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.Flow
/**
 * Extension property to retrieve the [ConnectivityManager] system service from the [Context].
 */
val Context.connectivityManager get(): ConnectivityManager {
    return getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

/**
 * Extension function on [ConnectivityManager] to observe network connectivity as a [Flow].
 *
 * This function creates a [Flow] that emits updates to the connectivity state of the device.
 * It registers a [NetworkCallback] to monitor changes in network connectivity (e.g., availability of
 * internet connection). The flow emits [ConnectionState.Available] or [ConnectionState.Unavailable]
 * based on the current network state.
 *
 * The flow will only emit distinct connectivity state changes due to the use of [distinctUntilChanged].
 *
 * @return A [Flow] emitting the current [ConnectionState] (Available or Unavailable).
 */
fun ConnectivityManager.observeConnectivityAsFlow() = callbackFlow {
    // Initial state: emit current connectivity state
    trySend(currentConnectivityState)

    // Create a callback that sends updates about network availability
    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    // Create a network request that listens for networks with internet capability
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    // Register the callback for network changes
    registerNetworkCallback(networkRequest, callback)

    // Close the flow and unregister the callback when the flow is closed
    awaitClose {
        unregisterNetworkCallback(callback)
    }
}.distinctUntilChanged()  // Ensures only distinct connectivity changes are emitted

/**
 * Extension property on [ConnectivityManager] to retrieve the current connectivity state.
 *
 * It checks whether the device is connected to a network with internet capability and returns
 * a [ConnectionState.Available] if connected, or [ConnectionState.Unavailable] if not connected.
 *
 * @return The current [ConnectionState], indicating the connectivity status of the device.
 */
val ConnectivityManager.currentConnectivityState: ConnectionState
    get() {
        val activeNetwork = activeNetwork  // Get the active network
        val networkCapabilities = getNetworkCapabilities(activeNetwork)  // Get network capabilities

        // Check if the network has internet capability
        val connected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Return the appropriate connectivity state based on availability
        return if (connected) ConnectionState.Available else ConnectionState.Unavailable
    }

/**
 * Factory function that creates a [ConnectivityManager.NetworkCallback] which triggers the provided
 * [callback] with a [ConnectionState] when the network connection changes.
 *
 * This callback will notify when the network becomes available, lost, or unavailable.
 *
 * @param callback The function to call with the updated [ConnectionState].
 * @return A [ConnectivityManager.NetworkCallback] that can be used to register with the [ConnectivityManager].
 */
fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        // Called when the network becomes available
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)  // Notify that the network is available
        }

        // Called when the network is lost
        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)  // Notify that the network is unavailable
        }

        // Called when the network becomes unavailable (e.g., disconnected from Wi-Fi)
        override fun onUnavailable() {
            callback(ConnectionState.Unavailable)  // Notify that the network is unavailable
        }
    }
}