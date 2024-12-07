package com.bibek.chatapplication.utils.message

import com.bibek.chatapplication.utils.SocketEvent

/**
 * Extracts the content from a text message based on the provided `SocketEvent` route prefix
 * and an optional suffix.
 *
 * This function checks if the given `text` starts with the `route` of the provided `SocketEvent`
 * and ends with the specified `suffix`. If both conditions are satisfied, it trims off the prefix
 * and suffix, returning the content in between.
 *
 * @param socketEvent The `SocketEvent` object which contains the route prefix used for matching.
 * @param suffix The suffix to check at the end of the `text`. Defaults to `"]"`.
 * @param text The input string from which content needs to be extracted.
 * @return The content extracted from the `text` after removing the prefix (route) and suffix.
 * @throws IllegalArgumentException If the `text` doesn't start with the event's route or end with the specified suffix.
 */
fun extractJsonContent(socketEvent: SocketEvent, suffix: String = "]", text: String): String {
    // Check if the text starts with the prefix (route) and ends with the suffix
    if (text.startsWith(socketEvent.route) && text.endsWith(suffix)) {
        // Extract the substring between the prefix and suffix
        return text.removePrefix(socketEvent.route).removeSuffix(suffix).trim()
    }
    throw IllegalArgumentException("Invalid input format -> event - ${socketEvent.route}  = $text")
}

/**
 * Creates a socket message string by concatenating the route from the provided `SocketEvent`
 * with the given `text` and an optional suffix.
 *
 * This function generates a formatted message string that starts with the event's `route`,
 * followed by the provided `text`, and ends with the specified `suffix`. This is useful for
 * sending messages in a socket communication protocol.
 *
 * @param event The `SocketEvent` object which provides the route to be used as a prefix in the message.
 * @param text The content of the message that will be inserted between the route and suffix.
 * @param suffix The suffix to append to the end of the message. Defaults to `"]"`.
 * @return A formatted string containing the event's route, the provided text, and the suffix.
 */
fun createSocketMessageString(event: SocketEvent, text: String, suffix: String = "]"): String {
    // Combine the event's route, text, and suffix to form the socket message
    return event.route + text + suffix
}
