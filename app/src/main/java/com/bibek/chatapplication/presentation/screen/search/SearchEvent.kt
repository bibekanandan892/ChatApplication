package com.bibek.chatapplication.presentation.screen.search

import com.bibek.chatapplication.presentation.screen.signup.Gender

sealed interface SearchEvent {
    data class OnNameChange(val name: String) : SearchEvent
    data object NavigateToChat : SearchEvent
    data object OnAcceptClick : SearchEvent
    data class OnGenderSelect(val gender: Gender) : SearchEvent
    data object OnRematchClick : SearchEvent
    data class GetUserDetails(val name: String, val gender: String, val preferGender: String) :
        SearchEvent
    data object SendMessage : SearchEvent
    data class OnCurrentMessageChange(val value : String) : SearchEvent
    data object OnLeaveChatClick : SearchEvent

}