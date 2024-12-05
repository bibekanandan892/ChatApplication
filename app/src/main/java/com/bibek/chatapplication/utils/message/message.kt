package com.bibek.chatapplication.utils.message

import com.bibek.chatapplication.utils.SocketEvent

fun extractJsonContent(socketEvent : SocketEvent, suffix : String ="]", text: String): String {
    // Check if the text starts with the prefix and ends with the suffix
    if (text.startsWith(socketEvent.route) && text.endsWith(suffix)) {
        // Extract the substring between the prefix and suffix
        return text.removePrefix(socketEvent.route).removeSuffix(suffix).trim()
    }
    throw IllegalArgumentException("Invalid input format -> event - ${socketEvent.route}  = $text")
}
fun createSocketMessageString(event : SocketEvent, text : String , suffix : String ="]"): String {
    return event.route + text  + suffix
}