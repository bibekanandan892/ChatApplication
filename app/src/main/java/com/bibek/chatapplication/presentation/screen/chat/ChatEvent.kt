package com.bibek.chatapplication.presentation.screen.chat

import com.bibek.chatapplication.presentation.screen.signup.Gender

sealed class ChatEvent {
    data object NavigateToSearch : ChatEvent()
    data class OnGenderSelect(val gender: Gender) : ChatEvent()
    data class GetUserDetails(val name: String, val gender: String) : ChatEvent()
    data object OnConversationsClick : ChatEvent()
    data object OnMyAccountClick  : ChatEvent()
}