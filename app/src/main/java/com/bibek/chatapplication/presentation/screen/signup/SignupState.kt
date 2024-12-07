package com.bibek.chatapplication.presentation.screen.signup

/**
 * A data class representing the state of the signup process.
 *
 * This class holds the necessary state values to track the progress and current status during the signup.
 *
 * @property isLoading A boolean indicating whether the signup process is currently loading.
 * @property gender The selected gender of the user during signup, or null if not selected.
 * @property udid A string representing the unique device identifier for the user.
 */
data class SignupState(
    val isLoading: Boolean = false,
    val gender: Gender? = null,
    val udid: String = ""
)

/**
 * A sealed class representing gender options for the user.
 *
 * This sealed class is used to define different gender options that can be selected by the user during the signup process.
 * It includes three predefined gender options: Male, Female, and Both.
 *
 * @property name The name of the gender, which will be displayed to the user (in Arabic).
 */
sealed class Gender(val name: String) {

    /**
     * Represents the male gender.
     */
    data object Male : Gender(name = "ذكر")

    /**
     * Represents the female gender.
     */
    data object Female : Gender(name = "أنثى")

    /**
     * Represents both genders.
     */
    data object Both : Gender(name = "الكل")
}
