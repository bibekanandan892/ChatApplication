package com.bibek.chatapplication.presentation.screen.search

import com.bibek.chatapplication.data.model.chat.ChatMessage
import com.bibek.chatapplication.presentation.screen.signup.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Represents the state of the search screen, including information about the user's match,
 * chat status, and the current message flow.
 *
 * @param chatState The current state of the chat (Matching, Matched, Accepted).
 * @param gender The gender preference selected by the user.
 * @param username The name of the current user.
 * @param matchUsername The username of the matched user.
 * @param chatId The unique ID of the chat.
 * @param isAccepted Whether the match has been accepted or not.
 * @param allChats A flow containing the list of all chat messages.
 * @param currentMessage The current message being typed by the user.
 * @param isRequestedForAccepted Flag indicating if the user has requested to accept the match.
 * @param isRequestAccepted Flag indicating if the match request has been accepted.
 * @param isShowDialog Flag indicating if a dialog should be shown (e.g., for exiting the chat).
 */
data class SearchState(
    val chatState: ChatState = ChatState.Matching,
    val gender: Gender? = null,
    val username: String = "",
    val matchUsername: String = "",
    val chatId: String = "",
    val isAccepted: Boolean = false,
    val allChats: Flow<List<ChatMessage>> = flowOf(),
    val currentMessage: String = "",
    val isRequestedForAccepted: Boolean = false,
    val isRequestAccepted: Boolean = false,
    val isShowDialog: Boolean = false
)

/**
 * Represents the different states of a chat.
 */
sealed interface ChatState {

    /**
     * The initial state when the user is looking for a match.
     */
    data object Matching : ChatState

    /**
     * The state when a match is found but not yet accepted.
     */
    data object Matched : ChatState

    /**
     * The state when the user has accepted the match and the chat is active.
     */
    data object Accepted : ChatState
}

/**
 * Represents the status of a message in the chat system.
 *
 * @param status A string representing the current status of the message (e.g., "sending", "sent", "read").
 */
sealed class Message(val status: String) {

    /**
     * Indicates that the message is currently being sent.
     */
    data object Sending : Message("sending")

    /**
     * Indicates that the message has been successfully sent.
     */
    data object Sent : Message("sent")

    /**
     * Indicates that the message has been read by the recipient.
     */
    data object Read : Message("read")

    /**
     * Represents a message with no status (e.g., a new or uninitialized message).
     */
    data object None : Message("")
}
