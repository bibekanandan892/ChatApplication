package com.bibek.chatapplication.presentation.screen.search
/**
 * Represents the various events that can occur in the chat screen's search flow.
 * These events trigger state changes or actions within the UI.
 */
sealed interface SearchEvent {

    /**
     * Event triggered when the user accepts a match.
     */
    data object OnAcceptClick : SearchEvent

    /**
     * Event triggered when the user chooses to rematch.
     */
    data object OnRematchClick : SearchEvent

    /**
     * Event triggered when a message is sent.
     */
    data object SendMessage : SearchEvent

    /**
     * Event triggered when the current message input is changed.
     *
     * @param value The updated message input.
     */
    data class OnCurrentMessageChange(val value: String) : SearchEvent

    /**
     * Event triggered when the user chooses to leave the chat.
     */
    data object OnLeaveChatClick : SearchEvent

    /**
     * Event triggered when the back button is clicked.
     */
    data object OnBackClick : SearchEvent

    /**
     * Event triggered when the dialog is dismissed.
     */
    data object OnDialogDismissClick : SearchEvent
}
