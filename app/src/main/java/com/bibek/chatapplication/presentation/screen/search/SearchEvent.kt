package com.bibek.chatapplication.presentation.screen.search

sealed interface SearchEvent {
    data object OnAcceptClick : SearchEvent
    data object OnRematchClick : SearchEvent
    data object SendMessage : SearchEvent
    data class OnCurrentMessageChange(val value: String) : SearchEvent
    data object OnLeaveChatClick : SearchEvent
    data object OnBackClick : SearchEvent
    data object OnDialogDismissClick : SearchEvent
}