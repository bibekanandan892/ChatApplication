package com.bibek.chatapplication.utils.logger

import android.util.Log
import com.bibek.chatapplication.BuildConfig
import com.bibek.chatapplication.utils.DATE_FORMAT_PATTERN
import com.bibek.chatapplication.utils.KTOR_CLIENT
import io.ktor.client.plugins.logging.Logger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NetworkLogger : Logger {
    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    override fun log(message: String) {
        if (BuildConfig.DEBUG) {
            val timestamp = dateFormat.format(Date())
            Log.d(KTOR_CLIENT, "[$timestamp] $message")
        }
    }
}