package com.bibek.chatapplication.presentation.screen.home

import com.bibek.chatapplication.presentation.screen.signup.Gender

sealed class HomeEvent {
    data object NavigateToSearch : HomeEvent()
    data class OnGenderSelect(val gender: Gender) : HomeEvent()
    data class GetUserDetails(val name: String, val gender: String) : HomeEvent()
    data object OnConversationsClick : HomeEvent()
    data object OnMyAccountClick  : HomeEvent()
}