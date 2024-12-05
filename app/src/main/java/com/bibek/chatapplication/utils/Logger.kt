package com.bibek.chatapplication.utils


import android.util.Log

object Logger {

    private const val GLOBAL_TAG = "Chat-Application-Logs"
    private var isLoggingEnabled: Boolean = true // Toggle logging globally

    /**
     * Log a message with the default tag and log level.
     * Automatically determines log level based on message length.
     */
    fun log(message: String) {
        if (isLoggingEnabled) {
            when {
                message.length <= 50 -> Log.d(GLOBAL_TAG, message) // Debug for short messages
                message.length <= 100 -> Log.i(GLOBAL_TAG, message) // Info for medium-length messages
                else -> Log.w(GLOBAL_TAG, message) // Warning for long messages
            }
        }
    }

    /**
     * Enable or disable logging globally.
     */
    fun setLoggingEnabled(enabled: Boolean) {
        isLoggingEnabled = enabled
    }
}
