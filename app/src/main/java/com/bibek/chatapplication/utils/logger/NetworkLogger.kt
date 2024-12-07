package com.bibek.chatapplication.utils.logger

import android.util.Log
import com.bibek.chatapplication.BuildConfig
import com.bibek.chatapplication.utils.DATE_FORMAT_PATTERN
import com.bibek.chatapplication.utils.KTOR_CLIENT
import io.ktor.client.plugins.logging.Logger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Custom implementation of the [Logger] interface for logging network-related messages.
 *
 * This class is designed to log network-related messages with a timestamp in the debug build only.
 * It uses the [SimpleDateFormat] to format the timestamp according to the specified pattern and
 * logs the message using Android's [Log.d] method. The log tag is provided by the constant [KTOR_CLIENT].
 *
 * It logs messages only when the application is in the debug build, controlled by the [BuildConfig.DEBUG] flag.
 */
class NetworkLogger : Logger {
    // Date formatter for formatting the timestamp in the log
    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    /**
     * Logs the network message with a timestamp.
     *
     * This function formats the current date and time based on [DATE_FORMAT_PATTERN] and then logs
     * the message with the formatted timestamp.
     *
     * @param message The network message to be logged.
     */
    override fun log(message: String) {
        // Check if the app is in debug mode before logging the message
        if (BuildConfig.DEBUG) {
            val timestamp = dateFormat.format(Date())  // Get the current timestamp
            Log.d(KTOR_CLIENT, "[$timestamp] $message")  // Log the message with timestamp
        }
    }
}
