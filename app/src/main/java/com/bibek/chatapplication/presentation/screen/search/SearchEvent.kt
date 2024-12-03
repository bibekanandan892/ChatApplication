package com.bibek.chatapplication.presentation.screen.search

import com.bibek.chatapplication.presentation.screen.signup.Gender

sealed class SearchEvent {
    data class OnNameChange(val name: String) : SearchEvent()
    data object NavigateToHome : SearchEvent()
    data class OnGenderSelect(val gender: Gender) : SearchEvent()
    data class GetUserDetails(val name: String, val gender: String, val preferGender: String) :
        SearchEvent()

}