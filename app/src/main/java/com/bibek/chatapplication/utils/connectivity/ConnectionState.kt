package com.bibek.chatapplication.utils.connectivity

/**
 * Sealed class representing the connectivity state of the device.
 *
 * This class is used to represent whether the device is connected to a network with internet
 * capability or not.
 */
sealed class ConnectionState {
    object Available : ConnectionState()  // Represents an internet-available state
    object Unavailable : ConnectionState()  // Represents an internet-unavailable state
}