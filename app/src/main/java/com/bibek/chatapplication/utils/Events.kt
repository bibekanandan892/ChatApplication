package com.bibek.chatapplication.utils

/**
 * A sealed class representing different types of socket events.
 *
 * This class defines the various events that can be emitted or received
 * through a WebSocket connection. Each event corresponds to a specific
 * action or state in the socket communication. The `route` string represents
 * the unique identifier or route for the event.
 *
 * The events defined include operations like session management,
 * status updates, message sending, acknowledgment, synchronization,
 * and more.
 *
 * Each event is represented by a `data object` that holds the route string
 * corresponding to that specific event type.
 *
 * @property route A string representing the route for the socket event.
 *
 * @sealed
 * @since 1.0
 */
sealed class SocketEvent(val route: String) {

    /**
     * Represents a session event route.
     */
    data object Session : SocketEvent("""["session",""")

    /**
     * Represents a status event route.
     */
    data object Status : SocketEvent("""["status",""")

    /**
     * Represents a match event route.
     */
    data object Match : SocketEvent("""["match",""")

    /**
     * Represents a matched event route.
     */
    data object Matched : SocketEvent("""["matched",""")

    /**
     * Represents a sync event route.
     */
    data object Sync : SocketEvent("""["sync",""")

    /**
     * Represents an accept event route.
     */
    data object Accept : SocketEvent("""["accept",""")

    /**
     * Represents a leave event route.
     */
    data object Leave : SocketEvent("""["leave",""")

    /**
     * Represents a message event route.
     */
    data object Message : SocketEvent("""["message",""")

    /**
     * Represents an acknowledgment event route.
     */
    data object Ack : SocketEvent("""["ack",""")

    /**
     * Represents a type event route.
     */
    data object Type : SocketEvent("""["type",""")

    /**
     * Represents a send event route.
     */
    data object Send : SocketEvent("""["send",""")

    /**
     * Represents a seen event route.
     */
    data object Seen : SocketEvent("""["seen",""")

    /**
     * Represents an OnOpen event route.
     */
    data object OnOpen : SocketEvent("OnOpen")

    /**
     * Represents an OnClosing event route.
     */
    data object OnClosing : SocketEvent("OnClose")

    /**
     * Represents an OnClosed event route.
     */
    data object OnClosed : SocketEvent("OnClosed")

    /**
     * Represents an OnFailure event route.
     */
    data object OnFailure : SocketEvent("OnFailure")
}
