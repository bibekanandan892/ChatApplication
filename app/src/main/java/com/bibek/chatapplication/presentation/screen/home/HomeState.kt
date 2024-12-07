package com.bibek.chatapplication.presentation.screen.home

import com.bibek.chatapplication.presentation.screen.signup.Gender

/**
 * Represents the state of the Home screen, holding user-related information and preferences.
 * This state is used to manage the UI of the Home screen and reflects the user's choices.
 *
 * @param gender The gender of the user (default is an empty string).
 * @param preferGender The gender preference of the user, represented as a [Gender] enum (default is null).
 * @param name The name of the user (default is an empty string).
 */
data class HomeState(
    val gender: String = "",
    val preferGender: Gender? = null,
    val name: String = ""
)
