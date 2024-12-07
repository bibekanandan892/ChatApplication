package com.bibek.chatapplication.presentation.screen.home

import com.bibek.chatapplication.presentation.screen.signup.Gender

/**
 * Sealed class representing different events for the Home screen.
 */
sealed class HomeEvent {

    /**
     * Event triggered to navigate to the search screen.
     */
    data object NavigateToSearch : HomeEvent()

    /**
     * Event triggered when a gender is selected.
     *
     * @param gender The gender selected by the user.
     */
    data class OnGenderSelect(val gender: Gender) : HomeEvent()

    /**
     * Event triggered to get user details.
     *
     * @param name The name of the user.
     * @param gender The gender of the user.
     */
    data class GetUserDetails(val name: String, val gender: String) : HomeEvent()

    /**
     * Event triggered when the conversations section is clicked.
     */
    data object OnConversationsClick : HomeEvent()

    /**
     * Event triggered when the "My Account" section is clicked.
     */
    data object OnMyAccountClick : HomeEvent()
}
