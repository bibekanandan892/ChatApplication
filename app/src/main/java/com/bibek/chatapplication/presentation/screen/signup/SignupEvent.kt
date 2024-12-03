package com.bibek.chatapplication.presentation.screen.signup

sealed class SignupEvent {
    data class OnNameChange(val name: String) : SignupEvent()
    data object NavigateToHome : SignupEvent()
    data class OnGenderSelect(val gender: Gender) : SignupEvent()
}