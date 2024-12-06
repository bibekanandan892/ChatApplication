package com.bibek.chatapplication.presentation.screen.signup

sealed interface SignupEvent {
    data class OnNameChange(val name: String) : SignupEvent
    data class OnSignupClick(val deviceId : String) : SignupEvent
    data class OnGenderSelect(val gender: Gender) : SignupEvent
}