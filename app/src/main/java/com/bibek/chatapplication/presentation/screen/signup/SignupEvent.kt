package com.bibek.chatapplication.presentation.screen.signup

/**
 * A sealed interface representing the different events that can occur during the signup process.
 *
 * This interface is used to encapsulate various user interactions and actions during the signup flow.
 * It is used to trigger appropriate responses or state changes in the signup process.
 */
sealed interface SignupEvent {

    /**
     * Event triggered when the user changes their name during signup.
     *
     * @property name The updated name entered by the user.
     */
    data class OnNameChange(val name: String) : SignupEvent

    /**
     * Event triggered when the user clicks the signup button.
     *
     * @property deviceId The unique device identifier associated with the signup attempt.
     */
    data class OnSignupClick(val deviceId: String) : SignupEvent

    /**
     * Event triggered when the user selects their gender during signup.
     *
     * @property gender The selected gender of the user.
     */
    data class OnGenderSelect(val gender: Gender) : SignupEvent
}
